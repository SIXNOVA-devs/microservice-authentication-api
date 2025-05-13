package pe.edu.utp.microservice_authentication_api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.microservice_authentication_api.domain.entities.InformacionEstudiante;

public interface InformacionEstudianteRepository extends JpaRepository<InformacionEstudiante, Long> {
    boolean existsByNumeroDocumento(String numeroDocumento);
}