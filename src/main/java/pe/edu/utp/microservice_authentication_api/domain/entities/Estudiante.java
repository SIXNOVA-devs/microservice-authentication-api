package pe.edu.utp.microservice_authentication_api.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "codigo_alumno")
    private String codigoAlumno;

    @Column(name = "ciclo_actual", nullable = false)
    private Integer cicloActual;

    @Column(name = "estado", length = 20)
    private String estado; // ACTIVO, INACTIVO, EGRESADO, etc.

    @Column(name = "carrera", length = 100)
    private String carrera;

    @Column(name = "facultad", length = 100)
    private String facultad;

    @Column(name = "modalidad", length = 20)
    private String modalidad; // REGULAR, OBLIGATORIO, LIBRE, etc.

    @Column(name = "campus", length = 20)
    private String campus; // REGULAR, OBLIGATORIO, LIBRE, etc.

    @Column(name = "fecha_inscripcion")
    private LocalDateTime fechaInscripcion;

    @Column(name = "fecha_ultima_matricula")
    private LocalDateTime fechaUltimaMatricula;

    @Column(name = "fecha_ultima_inscripcion")
    private LocalDateTime fechaUltimaInscripcion;

    @OneToOne(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private InformacionEstudiante informacionPersonal;

    @OneToOne(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Credenciales credenciales;
}
