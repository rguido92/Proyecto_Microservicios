package com.microservicios.usuarios.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private int usuario_id;

    public static String prueba;
    @Column(length = 10,name = "nombre")
    private String nombre;

    @Column(length = 80)
    private String correo_electronico;

    @Column(length = 80)
    private String direccion;

    @Column(length = 100)
    private String contrasena;

    public Usuario() {}

    public Usuario(String nombre, String correo_electronico, String direccion, String contrasena) {
        this.nombre = nombre;
        this.correo_electronico = correo_electronico;
        this.direccion = direccion;
        this.contrasena = contrasena;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
