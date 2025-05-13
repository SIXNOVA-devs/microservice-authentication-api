package pe.edu.utp.microservice_authentication_api.service;
import pe.edu.utp.microservice_authentication_api.dto.AuthResponse;

/**
 * Interfaz para servicios de autenticación con Firebase
 */
public interface FirebaseAuthService {
    
    /**
     * Autentica un usuario con email y contraseña
     * 
     * @param email El correo electrónico del usuario
     * @param password La contraseña del usuario
     * @return Un objeto AuthResponse con el resultado de la autenticación
     */
    AuthResponse signInWithEmailAndPassword(String email, String password);
}