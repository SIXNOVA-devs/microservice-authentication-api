package pe.edu.utp.microservice_authentication_api.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "pe.edu.utp.microservice_authentication_api.entity")
@EnableJpaRepositories(basePackages = "pe.edu.utp.microservice_authentication_api.repository")
public class MicroserviceAuthenticationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceAuthenticationApiApplication.class, args);
    }
}
