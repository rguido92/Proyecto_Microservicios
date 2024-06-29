
package com.microservicios.reservas.services;

import com.microservicios.reservas.dto.CrearHabitacionDTO;
import com.microservicios.reservas.dto.CrearReservaDTO;
import com.microservicios.reservas.dto.HabitacionDTO;
import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import com.microservicios.reservas.models.Reserva;
import com.microservicios.reservas.repositories.IHabitacionRepository;
import com.microservicios.reservas.repositories.IHotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HabitacionService {
    @Autowired
    private IHabitacionRepository habitacionRepository;
    @Autowired
    private IHotelRepository hotelRepository;

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

    public String crearHabitacion(CrearHabitacionDTO habitacionDTO) {
        if (comprobarContrasena(habitacionDTO.getNombre(), habitacionDTO.getContraseña())) {
            Habitacion habitacion = new Habitacion();
            habitacion.setHotel(hotelRepository.findById(habitacionDTO.getHotel_id()).orElse(null));
            habitacion.setTipo(habitacionDTO.getTipo());
            habitacion.setNumero_habitacion(habitacionDTO.getNumero_habitacion());
            habitacion.setDisponible(true);
            habitacion.setPrecio(habitacionDTO.getPrecio());
            this.habitacionRepository.save(habitacion);
            return "Habitacion creada";
        } else return "Usuario/Contraseña no valido";

    }

    public String actualizarHabitacion(CrearHabitacionDTO habitacionDTO) {
        if (comprobarContrasena(habitacionDTO.getNombre(), habitacionDTO.getContraseña())) {

            Habitacion habitacion = habitacionRepository.findById(habitacionDTO.getId());
            Hotel hotel = hotelRepository.findById(habitacionDTO.getHotel_id()).orElse(null);
            if (habitacion != null && hotel!=null) {
                habitacion.setHotel(hotel);
                habitacion.setTipo(habitacionDTO.getTipo());
                habitacion.setDisponible(habitacionDTO.isDisponible());
                habitacion.setPrecio(habitacionDTO.getPrecio());
                habitacion.setNumero_habitacion(habitacionDTO.getNumero_habitacion());
                habitacionRepository.save(habitacion);
                return "Habitacion actualizada";
            } else return "Habitacion/Hotel no valido";
        }else return "Usuario/Contraseña no valido";
    }

    public String eliminarHabitacion(int id) {
        Habitacion habitacion = habitacionRepository.findById(id);
        if (habitacion!=null){

            this.habitacionRepository.delete(habitacion);
            return "habitacion eliminada";
        }else{
            return "el id no es valido";
        }
    }

    public HabitacionDTO obtenerHabitacionPorId(int id) {
        return new HabitacionDTO(this.habitacionRepository.findById(id));
    }

    public Habitacion findById(int habitacionId) {
        return habitacionRepository.findById(habitacionId);
    }
}
