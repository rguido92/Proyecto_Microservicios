package com.microservicios.comentarios.dto;

import com.microservicios.comentarios.entidades.Comentario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ComentarioDTO {
    private String id;
    private int usuarioId;
    private int hotelId;
    private int reservaId;
    private double  puntuacion;
    private String comentario;
    private String fechaCreacion;

    public ComentarioDTO(Comentario comentario){
        this.id = comentario.getId();
        this.usuarioId= comentario.getUsuarioId();
        this.hotelId= comentario.getHotelId();
        this.reservaId= comentario.getReservaId();
        this.puntuacion= comentario.getPuntuacion();
        this.comentario=comentario.getComentario();
        this.fechaCreacion=comentario.getFechaCreacion().toString();
    }

}
