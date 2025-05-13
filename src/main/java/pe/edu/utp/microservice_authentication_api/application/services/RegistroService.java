package pe.edu.utp.microservice_authentication_api.application.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pe.edu.utp.microservice_authentication_api.domain.entities.*;
import pe.edu.utp.microservice_authentication_api.domain.repositories.CredencialesRepository;
import pe.edu.utp.microservice_authentication_api.domain.repositories.EstudianteRepository;
import pe.edu.utp.microservice_authentication_api.domain.repositories.InformacionEstudianteRepository;

import pe.edu.utp.microservice_authentication_api.application.dtos.RegisterRequestDTO;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class RegistroService {
    private static final String FIREBASE_SIGNUP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp";
    private final String API_KEY;
    private final RestTemplate restTemplate;
    private final CodigoEstudianteService codigoEstudianteService;
    private final EstudianteRepository estudianteRepository;
    private final InformacionEstudianteRepository informacionEstudianteRepository;
    private final CredencialesRepository credencialesRepository;

    public RegistroService(
            @Value("${firebase.api.key}") String apiKey,
            CodigoEstudianteService codigoEstudianteService,
            EstudianteRepository estudianteRepository,
            InformacionEstudianteRepository informacionEstudianteRepository,
            CredencialesRepository credencialesRepository) {
        this.API_KEY = apiKey;
        this.restTemplate = new RestTemplate();
        this.codigoEstudianteService = codigoEstudianteService;
        this.estudianteRepository = estudianteRepository;
        this.informacionEstudianteRepository = informacionEstudianteRepository;
        this.credencialesRepository = credencialesRepository;
    }

    @Transactional
    public Estudiante registrarEstudiante(RegisterRequestDTO request) {
        try {
            // 1. Validar documento
            if (informacionEstudianteRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
                throw new RuntimeException("Ya existe un estudiante con ese número de documento");
            }

            // 2. Generar código y correo
            String codigoAlumno = codigoEstudianteService.generarCodigoUnico();
            String correoCorporativo = generarCorreoCorporativo(codigoAlumno);

            // 3. Registrar en Firebase
            String url = FIREBASE_SIGNUP_URL + "?key=" + API_KEY;
            Map<String, String> firebaseRequest = Map.of(
                "email", correoCorporativo,
                "password", request.getPassword(),
                "returnSecureToken", "true"
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(url, firebaseRequest, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 4. Crear estudiante
                Estudiante estudiante = new Estudiante();
                estudiante.setCodigoAlumno(codigoAlumno);
                estudiante.setCarrera(request.getCarrera());
                estudiante.setFacultad(request.getFacultad());
                estudiante.setModalidad(request.getModalidad());
                estudiante.setCampus(request.getCampus());
                estudiante.setEstado("ACTIVO");
                estudiante.setCicloActual(1);
                estudiante.setFechaInscripcion(LocalDateTime.now());

                // 5. Crear y vincular información personal
                InformacionEstudiante infoEstudiante = new InformacionEstudiante();
                infoEstudiante.setEstudiante(estudiante);
                infoEstudiante.setNombres(request.getNombres());
                infoEstudiante.setApellidoPaterno(request.getApellidoPaterno());
                infoEstudiante.setApellidoMaterno(request.getApellidoMaterno());
                infoEstudiante.setTipoDocumento(request.getTipoDocumento());
                infoEstudiante.setNumeroDocumento(request.getNumeroDocumento());
                infoEstudiante.setCorreoPersonal(request.getCorreoPersonal());
                infoEstudiante.setFechaNacimiento(request.getFechaNacimiento());  // Add this line

                // 6. Crear y vincular credenciales
                Credenciales credenciales = new Credenciales();
                credenciales.setEstudiante(estudiante);
                credenciales.setCorreoCorporativo(correoCorporativo);
                credenciales.setPassword(request.getPassword());
                credenciales.setEstado("ACTIVO");

                // 7. Establecer relaciones bidireccionales
                estudiante.setInformacionPersonal(infoEstudiante);
                estudiante.setCredenciales(credenciales);

                // 8. Guardar entidad principal (cascadeará a las demás)
                log.info("Guardando estudiante con código: {}", codigoAlumno);
                Estudiante estudianteSaved = estudianteRepository.save(estudiante);
                log.info("Estudiante guardado exitosamente con ID: {}", estudianteSaved.getId());

                return estudianteSaved;
            } else {
                throw new RuntimeException("Error al crear usuario en Firebase");
            }

        } catch (Exception e) {
            log.error("Error en el registro: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la información del estudiante: " + e.getMessage());
        }
    }

    private String generarCorreoCorporativo(String codigoAlumno) {
        return codigoAlumno.toLowerCase() + "@utp.edu.pe";
    }
}