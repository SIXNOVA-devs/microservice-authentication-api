package pe.edu.utp.microservice_authentication_api.infraestructure.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.microservice_authentication_api.application.dtos.RegisterRequestDTO;
import pe.edu.utp.microservice_authentication_api.application.services.RegistroService;
import pe.edu.utp.microservice_authentication_api.domain.entities.Estudiante;

@RestController
@RequestMapping("/api/registro")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;

    @PostMapping
    public ResponseEntity<?> registrarEstudiante(@RequestBody RegisterRequestDTO request) {
        try {
            Estudiante estudiante = registroService.registrarEstudiante(request);
            return ResponseEntity.ok(new RegistroResponse(
                estudiante.getCodigoAlumno(),
                estudiante.getCredenciales().getCorreoCorporativo(),
                "Registro exitoso"
            ));
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Error en el registro: " + e.getMessage()));
        }
    }
}

record RegistroResponse(String codigoAlumno, String correoCorporativo, String mensaje) {}