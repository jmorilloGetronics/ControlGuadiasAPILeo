package com.leo.backend.model;

import java.util.List;

public class Bloque {
    private String franjaId;
    private List<ProfesorGuardia> profesoresGuardia;
    private List<AusenciaTarea> ausenciasTareas;

    public Bloque() {}

    public Bloque(String franjaId, List<ProfesorGuardia> profesoresGuardia, List<AusenciaTarea> ausenciasTareas) {
        this.franjaId = franjaId;
        this.profesoresGuardia = profesoresGuardia;
        this.ausenciasTareas = ausenciasTareas;
    }

    public String getFranjaId() {
        return franjaId;
    }

    public void setFranjaId(String franjaId) {
        this.franjaId = franjaId;
    }

    public List<ProfesorGuardia> getProfesoresGuardia() {
        return profesoresGuardia;
    }

    public void setProfesoresGuardia(List<ProfesorGuardia> profesoresGuardia) {
        this.profesoresGuardia = profesoresGuardia;
    }

    public List<AusenciaTarea> getAusenciasTareas() {
        return ausenciasTareas;
    }

    public void setAusenciasTareas(List<AusenciaTarea> ausenciasTareas) {
        this.ausenciasTareas = ausenciasTareas;
    }
}
