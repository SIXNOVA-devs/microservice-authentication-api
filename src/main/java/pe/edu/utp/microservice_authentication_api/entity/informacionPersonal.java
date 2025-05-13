package pe.edu.utp.microservice_authentication_api.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "informacion_personal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class informacionPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;
    private String apellidos;
    @Column(unique = true) // Asumiendo DNI único
    private String dni;
    private String telefono;
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    private String modalidad;
    private String carrera;
    private String campus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id", nullable = false) // FK a la tabla Estudiantes
    private estudiante estudiante;
}