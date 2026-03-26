package com.leo.backend.repository;

import jakarta.persistence.*;

@Entity
@Table(name = "ausencias")
public class AusenciaEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "fecha", length = 10, nullable = false)
    private String fecha;

    @Column(name = "franja_id", length = 20, nullable = false)
    private String franjaId;

    @Column(name = "profesor_ausente", length = 200, nullable = false)
    private String profesorAusente;

    @Column(name = "grupo", length = 50, nullable = false)
    private String grupo;

    @Column(name = "aula", length = 50, nullable = false)
    private String aula;

    @Column(name = "asignatura", length = 100, nullable = false)
    private String asignatura;

    @Column(name = "tarea", length = 500, nullable = false)
    private String tarea;

    public AusenciaEntity() {}

    public AusenciaEntity(String id, String fecha, String franjaId,
                          String profesorAusente, String grupo, String aula,
                          String asignatura, String tarea) {
        this.id = id;
        this.fecha = fecha;
        this.franjaId = franjaId;
        this.profesorAusente = profesorAusente;
        this.grupo = grupo;
        this.aula = aula;
        this.asignatura = asignatura;
        this.tarea = tarea;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getFranjaId() { return franjaId; }
    public void setFranjaId(String franjaId) { this.franjaId = franjaId; }

    public String getProfesorAusente() { return profesorAusente; }
    public void setProfesorAusente(String profesorAusente) { this.profesorAusente = profesorAusente; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }

    public String getTarea() { return tarea; }
    public void setTarea(String tarea) { this.tarea = tarea; }
}
