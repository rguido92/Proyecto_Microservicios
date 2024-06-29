package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Reserva;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTOFindByReservaUsuario {
    private int reserva_id;
    private int usuario;

    public ReservaDTOFindByReservaUsuario(Reserva reserva){
        this.reserva_id= reserva.getId();
        this.usuario= reserva.getUsuario();

    }
}
