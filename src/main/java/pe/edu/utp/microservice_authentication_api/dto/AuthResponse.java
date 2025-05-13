package pe.edu.utp.microservice_authentication_api.dto;

public record AuthResponse(
        boolean success,
        String token,
        String email,
        String uid,
        String errorMessage
) {}