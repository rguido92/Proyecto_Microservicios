package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Reserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListarReservasDTO {

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private int habitacion_id;

    public ListarReservasDTO(String nombre, String contrasena, Reserva reserva){
        this.fecha_inicio = reserva.getFecha_inicio();
        this.fecha_fin = reserva.getFecha_fin();
        this.habitacion_id= reserva.getHabitacion().getId();
    }
}
