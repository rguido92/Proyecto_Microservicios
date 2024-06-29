package com.microservicios.comentarios.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MostrarComentarioReservaDTO {
    private String nombre;
    private String contrasena;
    private Integer reservaId;

    public MostrarComentarioReservaDTO(String nombre, String contrasena, Integer reservaId) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.reservaId = reservaId;
    }
}