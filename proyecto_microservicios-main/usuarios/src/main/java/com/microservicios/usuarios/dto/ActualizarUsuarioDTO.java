package com.microservicios.usuarios.dto;

import com.microservicios.usuarios.entidades.Usuario;

public class ActualizarUsuarioDTO {
    private int id;
    private String nombre;
    private String correo_electronico;
    private String direccion;
    private String contrasena;

    public ActualizarUsuarioDTO() {}

    public ActualizarUsuarioDTO(Usuario usuario1) {
        this.id= usuario1.getUsuario_id();
        this.nombre=usuario1.getNombre();
        this.correo_electronico=usuario1.getCorreo_electronico();
        this.direccion= usuario1.getDireccion();
        this.contrasena= usuario1.getContrasena();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo_electronico() { return correo_electronico; }
    public void setCorreo_electronico(String correo_electronico) { this.correo_electronico = correo_electronico; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
