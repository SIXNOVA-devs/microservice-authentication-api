package pe.edu.utp.microservice_authentication_api.domain.entities;

import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credenciales")
@Data
@NoArgsConstructor
public class Credenciales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Formula("CONCAT(estudiante.codigo_alumno, '@utp.edu.pe')")
    @Column(name = "correo_corporativo", unique = true, nullable = false)
    private String correoCorporativo;

    @Formula("informacion_personal.correo_personal")
    @Column(name = "correo_recuperacion")
    private String correoRecuperacion;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "estado", length = 20)
    private String estado; // ACTIVO, INACTIVO, BLOQUEADO, etc.

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    private Estudiante estudiante;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        if (estudiante != null) {
            // Genera el correo corporativo basado en el código del alumno
            this.correoCorporativo = estudiante.getCodigoAlumno() + "@utp.edu.pe";
            
            // Obtiene el correo de recuperación desde la información personal
            if (estudiante.getInformacionPersonal() != null) {
                this.correoRecuperacion = estudiante.getInformacionPersonal().getCorreoPersonal();
            }
        }
    }

    // Constructor para facilitar la creación
    public Credenciales(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.estado = "ACTIVO";
        onSave(); // Genera los correos automáticamente
    }

    // Constructor for RegistroService
    public Credenciales(Estudiante estudiante, String password) {
        this.estudiante = estudiante;
        this.password = password;
        this.estado = "ACTIVO";
    }
}
