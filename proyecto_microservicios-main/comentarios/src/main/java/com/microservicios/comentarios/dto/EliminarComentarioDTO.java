package com.microservicios.comentarios.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EliminarComentarioDTO {
    private String nombre;
    private String contrasena;
    private String id;

    public EliminarComentarioDTO(String nombre, String contrasena, String id) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.id = id;
    }
}
