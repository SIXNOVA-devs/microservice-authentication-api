package pe.edu.utp.microservice_authentication_api.application.dtos;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}