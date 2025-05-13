package pe.edu.utp.microservice_authentication_api.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}