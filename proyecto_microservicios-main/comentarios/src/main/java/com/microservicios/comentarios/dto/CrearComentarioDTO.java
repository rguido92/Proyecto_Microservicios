package com.microservicios.comentarios.dto;

import com.microservicios.comentarios.entidades.Comentario;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class CrearComentarioDTO {
    private String nombreUsuario;
    private String contrasena;
   private String nombreHotel;
   private int reservaId;
   private double puntuacion;
   private String comentario;

    public CrearComentarioDTO(String nombreUsuario,String contrasena, String nombreHotel, Integer reservaId, Float puntuacion, String comentario) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreHotel = nombreHotel;
        this.reservaId = reservaId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

}
