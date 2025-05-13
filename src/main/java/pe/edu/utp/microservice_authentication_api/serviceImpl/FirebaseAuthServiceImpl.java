package pe.edu.utp.microservice_authentication_api.serviceImpl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import pe.edu.utp.microservice_authentication_api.dto.AuthResponse;
import pe.edu.utp.microservice_authentication_api.service.FirebaseAuthService;

/**
 * Implementación del servicio de autenticación con Firebase
 */
@Service
@Slf4j
public class FirebaseAuthServiceImpl implements FirebaseAuthService {

    private final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
    private final String API_KEY;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public FirebaseAuthServiceImpl(@Value("${firebase.api.key}") String apiKey) {
        this.API_KEY = apiKey;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AuthResponse signInWithEmailAndPassword(String email, String password) {
        try {
            log.debug("Attempting authentication for email: {}", email);
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
            return new AuthResponse(false, null, null, null, "Authentication failed");
        } catch (HttpClientErrorException e) {
            log.error("Authentication error: {}", e.getResponseBodyAsString());
            return extractFirebaseError(e);
        }
    }

    /**
     * Extrae información de error desde la excepción de Firebase
     * 
     * @param e La excepción HTTP generada por Firebase
     * @return Un objeto AuthResponse con la información del error
     */
    private AuthResponse extractFirebaseError(HttpClientErrorException e) {
        try {
            Map<String, Object> errorBody = objectMapper.readValue(
                e.getResponseBodyAsString(), Map.class
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

    /**
     * Convierte los códigos de error de Firebase en mensajes amigables para el usuario
     * 
     * @param errorCode El código de error de Firebase
     * @return Un mensaje de error en español
     */
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