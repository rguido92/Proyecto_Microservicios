package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Hotel;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
public class HotelDTO implements Serializable {
    private int hotel_id;
    private String nombre;
    private String direccion;
    private String usuario;
    private String contrasena;

    public HotelDTO(Hotel hotel) {
        this.hotel_id = hotel.getHotel_id();
        this.nombre = hotel.getNombre();
        this.direccion = hotel.getDireccion();
    }

    public HotelDTO(String nombre, String direccion) {

        this.nombre = nombre;
        this.direccion = direccion;
    }

    public HotelDTO(String usuario, String contrasena, String nombre, String direccion) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public HotelDTO(int hotel_id,String usuario, String contrasena, String nombre, String direccion) {
        this.hotel_id = hotel_id;
        this.nombre = nombre;
        this.direccion =direccion;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
}
