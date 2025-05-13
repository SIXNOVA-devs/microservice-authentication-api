package pe.edu.utp.microservice_authentication_api.application.services;

import pe.edu.utp.microservice_authentication_api.application.dtos.TokenValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Service
@Slf4j
public class TokenValidationService {
    private final String FIREBASE_USER_INFO_URL = "https://identitytoolkit.googleapis.com/v1/accounts:lookup";
    private final String API_KEY;
    private final RestTemplate restTemplate;

    public TokenValidationService(@Value("${firebase.api.key}") String apiKey) {
        this.API_KEY = apiKey;
        this.restTemplate = new RestTemplate();
    }

    public TokenValidationResponse validateToken(String idToken) {
        try {
            String url = FIREBASE_USER_INFO_URL + "?key=" + API_KEY;
            
            Map<String, String> request = Map.of("idToken", idToken);
            
            var response = restTemplate.postForEntity(url, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                var users = (java.util.List<Map<String, Object>>) response.getBody().get("users");
                if (users != null && !users.isEmpty()) {
                    var user = users.get(0);
                    return new TokenValidationResponse(
                        true,
                        (String) user.get("localId"),
                        (String) user.get("email"),
                        null
                    );
                }
            }
            return new TokenValidationResponse(false, null, null, "Token inválido");
            
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return new TokenValidationResponse(false, null, null, "Error validando token");
        }
    }
}