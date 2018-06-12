package com.example.roberto.reveles.Model;

public class Lugar {

    private String nombre;
    private String categoria;
    private String horario;
    private String descripcion;
    private String servicios;
    private String datos;

    public Lugar() {
    }

    public Lugar(String nombre, String categoria, String horario, String descripcion, String servicios, String datos) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.horario = horario;
        this.descripcion = descripcion;
        this.servicios = servicios;
        this.datos = datos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }
}
