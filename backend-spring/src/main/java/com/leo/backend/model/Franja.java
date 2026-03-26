package com.leo.backend.model;

public class Franja {
    private String id;
    private String etiqueta;
    private String inicio;
    private String fin;
    private int orden;
    private String tipo;

    public Franja() {}

    public Franja(String id, String etiqueta, String inicio, String fin, int orden) {
        this.id = id;
        this.etiqueta = etiqueta;
        this.inicio = inicio;
        this.fin = fin;
        this.orden = orden;
        this.tipo = "lectiva";
    }

    public Franja(String id, String etiqueta, String inicio, String fin, int orden, String tipo) {
        this.id = id;
        this.etiqueta = etiqueta;
        this.inicio = inicio;
        this.fin = fin;
        this.orden = orden;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
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

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
