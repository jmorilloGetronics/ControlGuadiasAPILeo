package com.leo.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "centro")
public class CentroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // Jornada embebida
    @Column(name = "jornada_inicio", nullable = false)
    private String jornadaInicio;

    @Column(name = "jornada_fin", nullable = false)
    private String jornadaFin;

    @Column(name = "jornada_horas", nullable = false)
    private int jornadaHoras;

    // Recreo embebido
    @Column(name = "recreo_inicio", nullable = false)
    private String recreoInicio;

    @Column(name = "recreo_fin", nullable = false)
    private String recreoFin;

    @Column(name = "recreo_duracion", nullable = false)
    private int recreoDuracion;

    public CentroEntity() {}

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getJornadaInicio() { return jornadaInicio; }
    public void setJornadaInicio(String jornadaInicio) { this.jornadaInicio = jornadaInicio; }
    public String getJornadaFin() { return jornadaFin; }
    public void setJornadaFin(String jornadaFin) { this.jornadaFin = jornadaFin; }
    public int getJornadaHoras() { return jornadaHoras; }
    public void setJornadaHoras(int jornadaHoras) { this.jornadaHoras = jornadaHoras; }
    public String getRecreoInicio() { return recreoInicio; }
    public void setRecreoInicio(String recreoInicio) { this.recreoInicio = recreoInicio; }
    public String getRecreoFin() { return recreoFin; }
    public void setRecreoFin(String recreoFin) { this.recreoFin = recreoFin; }
    public int getRecreoDuracion() { return recreoDuracion; }
    public void setRecreoDuracion(int recreoDuracion) { this.recreoDuracion = recreoDuracion; }
} 
