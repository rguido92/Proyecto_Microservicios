package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Reserva;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservaDTO {
    private int reservaId;
    private int usuarioId;
    private Habitacion habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    public ReservaDTO(Reserva reserva) {
        this.reservaId = reserva.getId();
        this.usuarioId = reserva.getId();
        this.habitacion = reserva.getHabitacion();
        this.fechaInicio = reserva.getFecha_inicio();
        this.fechaFin = reserva.getFecha_fin();
        this.estado = reserva.getEstado();
    }
}