package pe.edu.utp.microservice_authentication_api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.microservice_authentication_api.domain.entities.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    boolean existsByCodigoAlumno(String codigoAlumno);
}