package pe.edu.utp.microservice_authentication_api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.microservice_authentication_api.domain.entities.Credenciales;

public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {
    boolean existsByCorreoCorporativo(String correoCorporativo);
}