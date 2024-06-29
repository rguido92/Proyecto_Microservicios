package com.microservicios.reservas.services;

import com.microservicios.reservas.dto.*;
import com.microservicios.reservas.models.Hotel;
import com.microservicios.reservas.models.Reserva;
import com.microservicios.reservas.repositories.IHotelRepository;
import com.microservicios.reservas.repositories.IReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private HabitacionService habitacionService;
    @Autowired
    private IHotelRepository iHotelRepository;


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


    public String crearReserva(CrearReservaDTO reservaDTO) {
        // Verificar la contraseña del usuario
        if (comprobarContrasena(reservaDTO.getNombre(), reservaDTO.getContrasena())) {
            try {
                int usuarioId = obtenerIdUsuario(reservaDTO.getNombre());
                Reserva reserva = new Reserva();
                reserva.setUsuario(usuarioId); // Asignar el ID del usuario
                reserva.setFecha_inicio(reservaDTO.getFecha_inicio());
                reserva.setFecha_fin(reservaDTO.getFecha_fin());
                reserva.setHabitacion(habitacionService.findById(reservaDTO.getHabitacion_id()));
                reserva.setEstado(reservaDTO.getEstado());
                reservaRepository.save(reserva);
                return "Reserva creada correctamente";
            } catch (Exception e) {
                return "Error al crear la reserva: " + e.getMessage();
            }
        } else {
            return "Contraseña incorrecta";
        }
    }


    public String cambiarEstadoReserva(ReservaCambiarEstadoDTO cambioEstadoReservaDTO) {
        if (comprobarContrasena(cambioEstadoReservaDTO.getNombre(), cambioEstadoReservaDTO.getContrasena())) {
            try {
                Reserva reserva = reservaRepository.findById(cambioEstadoReservaDTO.getReserva_id());
                if (reserva != null) {
                    reserva.setEstado(cambioEstadoReservaDTO.getEstado());
                    reservaRepository.save(reserva);
                    return "Estado de la reserva cambiado" + cambioEstadoReservaDTO.getEstado();
                } else return "La reserva con ID " + cambioEstadoReservaDTO.getReserva_id() + "no se encontro";
            } catch (Exception e) {
                return "Error al cambiar el estado de la reserva" + e.getMessage();
            }
        } else return "";
    }

    public List<ListarReservasDTO> listarReserva(ValidarUsuarioDTO validarUsuarioDTO) {
        if (comprobarContrasena(validarUsuarioDTO.getNombre(), validarUsuarioDTO.getContrasena())) {
            //  http://localhost:8702/usuarios/info/
            List<ListarReservasDTO> listarReservasDTOS = new ArrayList<>();
            int usuario_id = obtenerIdUsuario(validarUsuarioDTO.getNombre());
            List<Reserva> reservas = reservaRepository.findByUsuario(usuario_id);
            ListarReservasDTO newReserva;
            for (Reserva reserva : reservas) {
                newReserva = new ListarReservasDTO();
                newReserva.setHabitacion_id(reserva.getHabitacion().getId());
                newReserva.setFecha_inicio(reserva.getFecha_inicio());
                newReserva.setFecha_fin(reserva.getFecha_fin());
                listarReservasDTOS.add(newReserva);
            }
            return listarReservasDTOS;
        }
        return null;
    }

    public List<ListarReservasDTO> findbyEstado(String estado) {
        List<Reserva> reservas = reservaRepository.findByEstado(estado);
        List<ListarReservasDTO> reservasDTO = new ArrayList<>();
        ListarReservasDTO reservasDTO1;
        for (Reserva reserva : reservas) {
            reservasDTO1 = new ListarReservasDTO();
            reservasDTO1.setFecha_inicio(reserva.getFecha_inicio());
            reservasDTO1.setFecha_fin(reserva.getFecha_fin());
            reservasDTO1.setHabitacion_id(reserva.getHabitacion().getId());
            reservasDTO.add(reservasDTO1);
        }
        return reservasDTO;
    }

    public boolean checkReserva(CheckReservaDTO checkReservaDTO) {
        Reserva reserva = reservaRepository.findcheckReserva(checkReservaDTO.getIdUsuario(), checkReservaDTO.getIdHotel(),checkReservaDTO.getIdReserva());
        if (reserva != null) return true;
        else return false;

    }


    public int obtenerIdUsuario(String nombre) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8702/usuarios/info/nombre/?nombre=" + nombre;
        try {
            //  GET al UsuarioService: respuesta como Integer
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, null, Integer.class);
            if (response.getBody() != null) {
                // Obtener el ID del usuario de la respuesta
                int usuarioId = response.getBody();
                System.out.println("ID del usuario obtenido correctamente: " + usuarioId);
                return usuarioId;
            } else {
                System.err.println("Error: Respuesta no exitosa o cuerpo de respuesta nulo");
                return 0; // Otra opción es lanzar una excepción si lo prefieres
            }
        } catch (Exception e) {
            System.err.println("Error al realizar la solicitud al servicio de usuarios: " + e.getMessage());
            return 0; // Otra opción es lanzar una excepción si lo prefieres
        }
    }


}


