package pe.edu.utp.microservice_authentication_api.domain.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "informacion_estudiantes")
@Data
@NoArgsConstructor
public class InformacionEstudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    // Información Personal Básica
    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;

    @Column(name = "apellido_paterno", length = 50, nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;

    @Column(name = "tipo_documento", length = 20, nullable = false)
    private String tipoDocumento;

    @Column(name = "numero_documento", length = 20, nullable = false, unique = true)
    private String numeroDocumento;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "estado_civil", length = 20)
    private String estadoCivil;

    // Dirección
    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "distrito", length = 50)
    private String distrito;

    @Column(name = "provincia", length = 50)
    private String provincia;

    @Column(name = "departamento", length = 50)
    private String departamento;

    // Contacto de Emergencia
    @Column(name = "contacto_emergencia_nombre", length = 100)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_relacion", length = 50)
    private String contactoEmergenciaRelacion;

    @Column(name = "contacto_emergencia_telefono", length = 15)
    private String contactoEmergenciaTelefono;

    @Column(name = "contacto_emergencia_direccion", length = 200)
    private String contactoEmergenciaDireccion;

    // Contacto Personal
    @Column(name = "telefono_fijo", length = 15)
    private String telefonoFijo;

    @Column(name = "telefono_movil", length = 15)
    private String telefonoMovil;

    @Column(name = "correo_personal", length = 100)
    private String correoPersonal;
}
