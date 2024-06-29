package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Reserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaCambiarEstadoDTO {

    private String nombre;
    private String contrasena;
    private int reserva_id;
    private String estado;

    public ReservaCambiarEstadoDTO(String nombre,String contrasena,Reserva reserva){
        this.nombre= nombre;
        this.contrasena= contrasena;
        this.reserva_id = reserva.getId();
        this.estado = reserva.getEstado();
    }
}
