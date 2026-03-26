package com.leo.backend.model;

import java.util.List;

public class Parte {
    private String fecha;
    private String diaSemana;
    private String usuarioConectado;
    private List<Bloque> bloques;

    public Parte() {}

    public Parte(String fecha, String diaSemana, String usuarioConectado, List<Bloque> bloques) {
        this.fecha = fecha;
        this.diaSemana = diaSemana;
        this.usuarioConectado = usuarioConectado;
        this.bloques = bloques;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getUsuarioConectado() {
        return usuarioConectado;
    }

    public void setUsuarioConectado(String usuarioConectado) {
        this.usuarioConectado = usuarioConectado;
    }

    public List<Bloque> getBloques() {
        return bloques;
    }

    public void setBloques(List<Bloque> bloques) {
        this.bloques = bloques;
    }
}
