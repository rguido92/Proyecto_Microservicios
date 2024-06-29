package com.microservicios.usuarios.dto;

import com.microservicios.usuarios.entidades.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioDTO {
    private int id;
    private String nombre;
    private String correo_electronico;
    private String direccion;
    private String contrasena;

    public ActualizarUsuarioDTO(Usuario usuario1) {
        this.id= usuario1.getUsuario_id();
        this.nombre=usuario1.getNombre();
        this.correo_electronico=usuario1.getCorreo_electronico();
        this.direccion= usuario1.getDireccion();
        this.contrasena= usuario1.getContrasena();
    }
}
