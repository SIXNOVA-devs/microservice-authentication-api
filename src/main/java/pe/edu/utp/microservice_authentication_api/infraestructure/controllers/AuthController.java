package pe.edu.utp.microservice_authentication_api.infraestructure.controllers;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.microservice_authentication_api.application.dtos.AuthRequestDTO;

import pe.edu.utp.microservice_authentication_api.application.dtos.AuthResponse;
import pe.edu.utp.microservice_authentication_api.application.services.FirebaseAuthService;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final FirebaseAuthService firebaseAuthService;

    public AuthController(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        try {
            // Validación básica
            if (request.getCode() == null || request.getPassword() == null) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("Código de estudiante y contraseña son requeridos"));
            }

            // Validar formato del código
            if (!isValidStudentCode(request.getCode())) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("Formato de código de estudiante inválido"));
            }

            // Intenta autenticar
            AuthResponse authResponse = firebaseAuthService.signInWithEmailAndPassword(
                    request.getCode(),
                    request.getPassword()
            );

            if (authResponse.success()) {
                return ResponseEntity.ok(new LoginResponse(
                        authResponse.token(),
                        authResponse.email(),
                        authResponse.uid()
                ));
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse(authResponse.errorMessage()));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor"));
        }
    }

    private boolean isValidStudentCode(String code) {
        // Validar formato: U + 9 dígitos
        return code.matches("^[Uu]\\d{8}$");
    }
}

record LoginResponse(String token, String email, String uid) {}
record ErrorResponse(String error) {}