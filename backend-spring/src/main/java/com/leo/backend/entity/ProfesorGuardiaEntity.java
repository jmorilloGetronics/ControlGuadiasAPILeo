package com.leo.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profesor_guardia")
public class ProfesorGuardiaEntity {

    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false)
    private String fecha;

    @Column(name = "franja_id", nullable = false)
    private String franjaId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ubicacion;

    public ProfesorGuardiaEntity() {}

    public ProfesorGuardiaEntity(String id, String fecha, String franjaId, String nombre, String ubicacion) {
        this.id = id;
        this.fecha = fecha;
        this.franjaId = franjaId;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getFranjaId() { return franjaId; }
    public void setFranjaId(String franjaId) { this.franjaId = franjaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}