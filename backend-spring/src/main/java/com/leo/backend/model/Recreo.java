package com.leo.backend.model;

public class Recreo {
    private String inicio;
    private String fin;
    private int duracionMinutos;

    public Recreo() {}

    public Recreo(String inicio, String fin, int duracionMinutos) {
        this.inicio = inicio;
        this.fin = fin;
        this.duracionMinutos = duracionMinutos;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
}
