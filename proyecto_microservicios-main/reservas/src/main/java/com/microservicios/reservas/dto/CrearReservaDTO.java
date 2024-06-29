package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Reserva;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class CrearReservaDTO {
    private String nombre;
    private String contrasena;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private int habitacion_id;
    @Pattern(regexp = ("Pendiente|Confirmada|Cancelada"),message = "Debes introducir un estado valido")
    private String estado;

    public CrearReservaDTO(String nombre,String contrasena,Reserva reserva){
        this.nombre= nombre;
        this.contrasena= contrasena;
        this.fecha_inicio= reserva.getFecha_inicio();
        this.fecha_fin = reserva.getFecha_fin();
        this.habitacion_id = reserva.getHabitacion().getId();
        this.estado=reserva.getEstado();
    }
}
