package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearHabitacionDTO implements Serializable {
    private String nombre;
    private String contraseña;

    private int id;
    private int hotel_id;
    private int numero_habitacion;
    @Pattern(regexp = ("Individual|Doble|Triple|Suite"),message = "Debes introducir un tipo de habitacion valido")

    private String tipo ;
    private BigDecimal precio;
    private boolean disponible;
    private CrearHabitacionDTO(Habitacion habitacion,String nombre,String contraseña){
        this.nombre= nombre;
        this.contraseña= contraseña;
        this.hotel_id= habitacion.getHotel().getHotel_id();
        this.numero_habitacion= habitacion.getNumero_habitacion();
        this.tipo= habitacion.getTipo();
        this.precio= habitacion.getPrecio();
    }

    public CrearHabitacionDTO(boolean disponible, BigDecimal precio, String tipo, int numero_habitacion, int hotel_id, int id, String contraseña, String nombre) {
        this.disponible = disponible;
        this.precio = precio;
        this.tipo = tipo;
        this.numero_habitacion = numero_habitacion;
        this.hotel_id = hotel_id;
        this.id = id;
        this.contraseña = contraseña;
        this.nombre = nombre;
    }
}
