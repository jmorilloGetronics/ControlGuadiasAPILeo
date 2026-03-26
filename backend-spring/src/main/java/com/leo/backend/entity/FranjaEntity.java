package com.leo.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "franja")
public class FranjaEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String id;          // "h1", "h2", "recreo", etc.

    @Column(nullable = false)
    private String etiqueta;    // "Hora 1", "Recreo", etc.

    @Column(name = "hora_inicio", nullable = false)
    private String horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private String horaFin;

    @Column(nullable = false)
    private int orden;

    @Column
    private String tipo;        // null = normal, "descanso" = recreo

    public FranjaEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}