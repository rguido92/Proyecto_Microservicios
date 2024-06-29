package com.microservicios.reservas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserpassDTO {
    private String nombre;
    private String contraseña;

    public UserpassDTO(String nombre, String contraseña) {
        this.nombre = nombre;
        this.contraseña = contraseña;
    }
}
