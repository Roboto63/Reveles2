package com.example.roberto.reveles.Model;

public class Usuario {

    private String nombre;
    private String correo;
    private String contraseña;
    private String foto;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String contraseña, String foto) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
