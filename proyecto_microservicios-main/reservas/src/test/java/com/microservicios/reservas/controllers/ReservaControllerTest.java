package com.microservicios.reservas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.reservas.dto.*;
import com.microservicios.reservas.services.HabitacionService;
import com.microservicios.reservas.services.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private HabitacionService habitacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearReserva_ShouldReturnOk() throws Exception {
        CrearReservaDTO dto = new CrearReservaDTO();
        dto.setNombre("testUser");
        dto.setContrasena("pass");
        dto.setFecha_inicio(LocalDate.now());
        dto.setFecha_fin(LocalDate.now().plusDays(2));
        dto.setHabitacion_id(1);
        dto.setEstado("Pendiente");

        when(reservaService.crearReserva(any(CrearReservaDTO.class))).thenReturn("Reserva creada correctamente");

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva creada correctamente"));
    }

    @Test
    void cambiarEstadoReserva_ShouldReturnOk() throws Exception {
        ReservaCambiarEstadoDTO dto = new ReservaCambiarEstadoDTO();
        dto.setNombre("testUser");
        dto.setContrasena("pass");
        dto.setReserva_id(1);
        dto.setEstado("Confirmada");

        when(reservaService.cambiarEstadoReserva(any(ReservaCambiarEstadoDTO.class)))
                .thenReturn("Estado de la reserva cambiado Confirmada");

        mockMvc.perform(patch("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Estado de la reserva cambiado Confirmada"));
    }

    @Test
    void checkReserva_ShouldReturnBoolean() throws Exception {
        when(reservaService.checkReserva(any(CheckReservaDTO.class))).thenReturn(true);

        mockMvc.perform(get("/reservas/check")
                        .param("idUsuario", "1")
                        .param("idReserva", "1")
                        .param("idHotel", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void listarReservasEstado_ShouldReturnList_WhenValid() throws Exception {
        ValidarUsuarioDTO userDto = new ValidarUsuarioDTO("testUser", "pass");
        ListarReservasDTO r = new ListarReservasDTO(LocalDate.now(), LocalDate.now().plusDays(1), 1);
        when(reservaService.comprobarContrasena("testUser", "pass")).thenReturn(true);
        when(reservaService.findbyEstado("Pendiente")).thenReturn(List.of(r));

        mockMvc.perform(get("/reservas/estado")
                        .param("estado", "Pendiente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isAccepted());
    }

    @Test
    void listarReservasEstado_ShouldReturnUnauthorized_WhenInvalid() throws Exception {
        ValidarUsuarioDTO userDto = new ValidarUsuarioDTO("testUser", "wrong");
        when(reservaService.comprobarContrasena("testUser", "wrong")).thenReturn(false);

        mockMvc.perform(get("/reservas/estado")
                        .param("estado", "Pendiente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isUnauthorized());
    }
}
