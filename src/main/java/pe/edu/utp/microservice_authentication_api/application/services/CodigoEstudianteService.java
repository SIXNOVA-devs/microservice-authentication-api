package pe.edu.utp.microservice_authentication_api.application.services;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

@Service
public class CodigoEstudianteService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional(readOnly = true)
    public String generarCodigoUnico() {
        String codigo;
        do {
            codigo = generarCodigo();
        } while (existeCodigo(codigo));
        
        return codigo;
    }
    
    private String generarCodigo() {
        Random random = new Random();
        int numero = random.nextInt(900000) + 100000; // Genera número de 6 dígitos
        int anio = java.time.Year.now().getValue() % 100; // Últimos 2 dígitos del año
        return String.format("U%d%d", anio, numero);
    }
    
    private boolean existeCodigo(String codigo) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(e) FROM Estudiante e WHERE e.codigoAlumno = :codigo", Long.class)
            .setParameter("codigo", codigo)
            .getSingleResult();
        return count > 0;
    }
}