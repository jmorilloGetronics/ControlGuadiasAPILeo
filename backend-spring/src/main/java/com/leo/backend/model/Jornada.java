package com.leo.backend.model;

public class Jornada {
    private String inicio;
    private String fin;
    private int horasLectivas;
    private Recreo recreo;

    public Jornada() {}

    public Jornada(String inicio, String fin, int horasLectivas, Recreo recreo) {
        this.inicio = inicio;
        this.fin = fin;
        this.horasLectivas = horasLectivas;
        this.recreo = recreo;
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

    public int getHorasLectivas() {
        return horasLectivas;
    }

    public void setHorasLectivas(int horasLectivas) {
        this.horasLectivas = horasLectivas;
    }

    public Recreo getRecreó() {
        return recreo;
    }

    public void setRecreó(Recreo recreo) {
        this.recreo = recreo;
    }
}
