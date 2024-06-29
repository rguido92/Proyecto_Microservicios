package com.microservicios.usuarios.dto;


import com.microservicios.usuarios.entidades.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * El patrón DTO tiene como finalidad crear un objeto plano (POJO) con una serie de atributos que puedan ser enviados o recuperados del servidor en una sola invocación.
 *
 * De tal forma que un DTO puede contener información de múltiples fuentes o tablas y concentrarlas en una única clase simple.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private String nombre;
    private String correo_electronico;
    private String direccion;
    private String contrasena;

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

}
