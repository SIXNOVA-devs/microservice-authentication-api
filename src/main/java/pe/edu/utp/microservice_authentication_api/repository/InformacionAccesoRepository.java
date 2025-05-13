package pe.edu.utp.microservice_authentication_api.repository;

import pe.edu.utp.microservice_authentication_api.entity.informacionAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformacionAccesoRepository extends JpaRepository<informacionAcceso, Long> {
    Optional<informacionAcceso> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
