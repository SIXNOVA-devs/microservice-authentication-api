package pe.edu.utp.microservice_authentication_api.application.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import pe.edu.utp.microservice_authentication_api.application.dtos.AuthResponse;

@Service
@Slf4j
public class FirebaseAuthService {
    private final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
    private final String API_KEY;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public FirebaseAuthService(@Value("${firebase.api.key}") String apiKey) {
        this.API_KEY = apiKey;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public AuthResponse signInWithEmailAndPassword(String code, String password) {
        try {
            // Convertir código a email corporativo
            String email = generateCorporateEmail(code);
            log.debug("Attempting authentication for code: {} with email: {}", code, email);
            
            String url = FIREBASE_AUTH_URL + "?key=" + API_KEY;
            Map<String, String> request = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", "true"
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return new AuthResponse(
                    true,
                    (String) response.getBody().get("idToken"),
                    email,
                    (String) response.getBody().get("localId"),
                    null
                );
            }

            return new AuthResponse(false, null, null, null, "Autenticación fallida");

        } catch (HttpClientErrorException e) {
            log.error("Authentication error: {}", e.getResponseBodyAsString());
            return extractFirebaseError(e);
        }
    }

    private String generateCorporateEmail(String code) {
        return code.toLowerCase() + "@utp.edu.pe";
    }

    private AuthResponse extractFirebaseError(HttpClientErrorException e) {
        try {
            Map<String, Object> errorBody = objectMapper.readValue(
                e.getResponseBodyAsString(), 
                Map.class
            );
            
            Map<String, Object> error = (Map<String, Object>) errorBody.get("error");
            String message = getFirebaseErrorMessage((String) error.get("message"));
            
            log.debug("Firebase error code: {}", error.get("message"));
            return new AuthResponse(false, null, null, null, message);
            
        } catch (Exception ex) {
            log.error("Error parsing Firebase error response", ex);
            return new AuthResponse(false, null, null, null, "Error de autenticación");
        }
    }

    private String getFirebaseErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "INVALID_LOGIN_CREDENTIALS" -> "Credenciales inválidas. Por favor, verifique su correo y contraseña";
            case "EMAIL_NOT_FOUND" -> "El correo electrónico no está registrado";
            case "INVALID_PASSWORD" -> "La contraseña es incorrecta";
            case "USER_DISABLED" -> "La cuenta ha sido deshabilitada";
            case "EMAIL_EXISTS" -> "El correo electrónico ya está en uso";
            case "OPERATION_NOT_ALLOWED" -> "El inicio de sesión está deshabilitado";
            case "TOO_MANY_ATTEMPTS_TRY_LATER" -> "Demasiados intentos fallidos. Intente más tarde";
            default -> "Error de autenticación: " + errorCode;
        };
    }
}