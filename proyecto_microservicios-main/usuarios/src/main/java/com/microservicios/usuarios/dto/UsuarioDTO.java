package com.microservicios.usuarios.dto;

import com.microservicios.usuarios.entidades.Usuario;

public class UsuarioDTO {

    private String nombre;
    private String correo_electronico;
    private String direccion;
    private String contrasena;

    public UsuarioDTO() {}

    public UsuarioDTO(Usuario usuario1) {
        this.nombre=usuario1.getNombre();
        this.correo_electronico=usuario1.getCorreo_electronico();
        this.direccion= usuario1.getDireccion();
        this.contrasena= usuario1.getContrasena();
    }
    public UsuarioDTO(String nombre ,String contrasena) {
        this.nombre=nombre;
        this.contrasena= contrasena;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo_electronico() { return correo_electronico; }
    public void setCorreo_electronico(String correo_electronico) { this.correo_electronico = correo_electronico; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
