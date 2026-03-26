package com.leo.backend.model;

import java.util.UUID;

public class AusenciaTarea {
    private String id;
    private String profesorAusente;
    private String grupo;
    private String aula;
    private String asignatura;
    private String tarea;

    public AusenciaTarea() {
        this.id = UUID.randomUUID().toString();
    }

    public AusenciaTarea(String profesorAusente, String grupo, String aula, String asignatura, String tarea) {
        this.id = UUID.randomUUID().toString();
        this.profesorAusente = profesorAusente;
        this.grupo = grupo;
        this.aula = aula;
        this.asignatura = asignatura;
        this.tarea = tarea;
    }

    public AusenciaTarea(String id, String profesorAusente, String grupo, String aula, String asignatura, String tarea) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
        this.profesorAusente = profesorAusente;
        this.grupo = grupo;
        this.aula = aula;
        this.asignatura = asignatura;
        this.tarea = tarea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
    }

    public String getProfesorAusente() {
        return profesorAusente;
    }

    public void setProfesorAusente(String profesorAusente) {
        this.profesorAusente = profesorAusente;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }
}
