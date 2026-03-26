package com.leo.backend.model;

public class Centro {
    private String nombre;
    private Jornada jornada;

    public Centro() {}

    public Centro(String nombre, Jornada jornada) {
        this.nombre = nombre;
        this.jornada = jornada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }
}
