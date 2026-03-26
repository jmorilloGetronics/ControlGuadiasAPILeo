package com.leo.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "parte")
public class ParteEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String fecha;           // "2026-02-10" — clave natural

    @Column(name = "dia_semana", nullable = false)
    private String diaSemana;

    @Column(name = "jefe_estudios")
    private String jefeEstudios;

    public ParteEntity() {}

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public String getJefeEstudios() { return jefeEstudios; }
    public void setJefeEstudios(String jefeEstudios) { this.jefeEstudios = jefeEstudios; }
}