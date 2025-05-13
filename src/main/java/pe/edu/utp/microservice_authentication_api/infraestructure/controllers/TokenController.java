package pe.edu.utp.microservice_authentication_api.infraestructure.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.microservice_authentication_api.application.services.TokenValidationService;
import pe.edu.utp.microservice_authentication_api.application.dtos.TokenValidationResponse;

@RestController
@RequestMapping("/api/token")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TokenController {

    private final TokenValidationService tokenValidationService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extraer el token del header (Bearer token)
            String token = authHeader.replace("Bearer ", "");
            
            // Validar el token
            TokenValidationResponse response = tokenValidationService.validateToken(token);
            
            if (response.valid()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(response);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(new TokenValidationResponse(false, null, null, "Error interno del servidor"));
        }
    }
}