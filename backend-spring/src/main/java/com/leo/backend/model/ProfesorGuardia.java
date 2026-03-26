package com.leo.backend.model;

public class ProfesorGuardia {
    private String nombre;
    private String ubicacion;

    public ProfesorGuardia() {}

    public ProfesorGuardia(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
