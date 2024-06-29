package com.microservicios.comentarios.dto;


import com.microservicios.comentarios.entidades.Comentario;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ListarComentariosHotelDTO {
    private String nombre;
    private String contrasena;
    private Integer idHotel;
    private String nombreHotel;
    private Integer reservaId;
    private Double puntuacion;
    private String comentario;

    public ListarComentariosHotelDTO(String nombre, String contrasena, String nombreHotel) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.nombreHotel = nombreHotel;
    }

    public ListarComentariosHotelDTO(String nombreHotel, Integer reservaId, Double puntuacion, String comentario) {
        this.nombreHotel = nombreHotel;
        this.reservaId = reservaId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    public ListarComentariosHotelDTO(Comentario comentarios) {
        setIdHotel(comentarios.getHotelId());
        setReservaId(comentarios.getReservaId());
        setPuntuacion(comentarios.getPuntuacion());
        setComentario(comentarios.getComentario());
    }
}
