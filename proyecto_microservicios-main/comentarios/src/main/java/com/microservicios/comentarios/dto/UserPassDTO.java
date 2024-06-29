package com.microservicios.comentarios.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.catalina.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserPassDTO {
    private String nombre;
    private String contrasena;
    public UserPassDTO(String nombre){
        this.nombre=nombre;
    }
}
