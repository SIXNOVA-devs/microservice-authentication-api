package pe.edu.utp.microservice_authentication_api.application.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequestDTO {
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String carrera;
    private String facultad;
    private String modalidad;
    private String campus;
    private String correoPersonal;
    private String password;
    private LocalDate fechaNacimiento;  // Add this field
}