package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {
    private int id;
    private Hotel hotel;
    private int numero_habitacion;
    private String tipo ;
    private BigDecimal precio;
    private boolean disponible;

    public HabitacionDTO(Habitacion habitacion){
        this.id=habitacion.getId();
        this.hotel= habitacion.getHotel();
        this.numero_habitacion= habitacion.getNumero_habitacion();
        this.tipo= habitacion.getTipo();
        this.precio= habitacion.getPrecio();
        this.disponible=habitacion.isDisponible();
    }
}
