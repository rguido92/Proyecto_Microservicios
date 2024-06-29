package com.microservicios.reservas.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class CheckReservaDTO {

    private int idUsuario;
    private int idHotel;
    private int idReserva;
}
