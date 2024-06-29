package com.microservicios.reservas.services;

import com.microservicios.reservas.dto.CrearReservaDTO;
import com.microservicios.reservas.dto.HotelDTO;
import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import com.microservicios.reservas.repositories.IHabitacionRepository;
import com.microservicios.reservas.repositories.IHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    @Autowired
    private IHotelRepository hotelRepository;
    @Autowired
    private IHabitacionRepository habitacionRepository;
    public boolean comprobarContrasena(String nombre, String contrasena) {
        RestTemplate restTemplate = new RestTemplate();
        String urlValidarContrasena = "http://localhost:8702/usuarios/validar";

        CrearReservaDTO crearReservaDTO = new CrearReservaDTO();
        crearReservaDTO.setNombre(nombre);
        crearReservaDTO.setContrasena(contrasena);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CrearReservaDTO> request = new HttpEntity<>(crearReservaDTO, headers);
        ResponseEntity<Boolean> response = restTemplate.postForEntity(urlValidarContrasena, request, Boolean.class);
        if (response.getBody() != null) {
            return response.getBody();
        } else {
            return false;
        }
    }
    public String crearHotel(HotelDTO hotelDTO) {
        try {
            Hotel hotel = new Hotel();
            hotel.setNombre(hotelDTO.getNombre());
            hotel.setDireccion(hotelDTO.getDireccion());
            hotelRepository.save(hotel);
            return "Hotel creado correctamente";
        } catch (Exception e) {
            return "Error al crear el hotel: " + e.getMessage();
        }
    }

    public String actualizarHotel(HotelDTO hotelDTO) {
        try {
            Hotel hotel = hotelRepository.findById(hotelDTO.getHotel_id()).orElse(null);
            if (hotel != null) {
                hotel.setNombre(hotelDTO.getNombre());
                hotel.setDireccion(hotelDTO.getDireccion());
                hotelRepository.save(hotel);
                return "Hotel actualizado correctamente";
            } else {
                return "Hotel no encontrado";
            }
        } catch (Exception e) {
            return "Error al actualizar el hotel: " + e.getMessage();
        }
    }

    public String eliminarHotel(int id) {
        try {
            Hotel hotel = hotelRepository.findById(id).orElse(null);
            if (hotel != null) {
                hotelRepository.delete(hotel);
                return "Hotel eliminado correctamente";
            } else {
                return "Hotel no encontrado";
            }
        } catch (Exception e) {
            return "Error al eliminar el hotel: " + e.getMessage();
        }
    }

    public int obtenerIdApartirNombre(String nombre) {
        try {
            Hotel hotel = hotelRepository.findByNombre(nombre);
            if (hotel != null) {
                return  hotel.getHotel_id();
            }
        } catch (Exception e) {
        }
        return 0;
    }

    public String obtenerNombreAPartirId(int id) {
        try {
            Hotel hotel = hotelRepository.findById(id).orElse(null);
            if (hotel != null) {
                return "Nombre del hotel con ID " + id + ": " + hotel.getNombre();
            } else {
                return "Hotel no encontrado con ID " + id;
            }
        } catch (Exception e) {
            return "Error al obtener el nombre del hotel a partir del ID: " + e.getMessage();
        }
    }

}
