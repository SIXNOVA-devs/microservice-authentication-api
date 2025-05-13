package pe.edu.utp.microservice_authentication_api.application.dtos;

public record TokenValidationResponse(
    boolean valid,
    String uid,
    String email,
    String error
) {}