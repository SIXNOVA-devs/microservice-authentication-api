package pe.edu.utp.microservice_authentication_api.repository;

import pe.edu.utp.microservice_authentication_api.entity.estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface EstudianteRepository extends JpaRepository<estudiante, Long> {
    Optional<estudiante> findByCodigoAlumno(String codigoAlumno);
    Optional<estudiante> findByInformacionAccesoCorreo(String correo); 
}