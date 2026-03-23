package com.microservicios.reservas.dto;

import com.microservicios.reservas.models.Hotel;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
public class HotelDTO implements Serializable {
    private int hotelId;
    private String nombre;
    private String direccion;
    private String usuario;
    private String contrasena;

    public HotelDTO(Hotel hotel) {
        this.hotelId = hotel.getHotelId();
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

    public HotelDTO(int hotelId,String usuario, String contrasena, String nombre, String direccion) {
        this.hotelId = hotelId;
        this.nombre = nombre;
        this.direccion =direccion;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
}
