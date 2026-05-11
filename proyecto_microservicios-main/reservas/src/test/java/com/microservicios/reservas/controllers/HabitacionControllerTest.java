package com.microservicios.reservas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.reservas.dto.CrearHabitacionDTO;
import com.microservicios.reservas.dto.HabitacionDTO;
import com.microservicios.reservas.dto.UserpassDTO;
import com.microservicios.reservas.services.HabitacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HabitacionContrller.class)
class HabitacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HabitacionService habitacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearHabitacion_ShouldReturnOk() throws Exception {
        CrearHabitacionDTO dto = new CrearHabitacionDTO(
                true, BigDecimal.valueOf(100), "Individual", 101, 1, 0, "admin123", "admin");

        when(habitacionService.crearHabitacion(any(CrearHabitacionDTO.class)))
                .thenReturn("Habitacion creada");

        mockMvc.perform(post("/reservas/habitacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Habitacion creada"));
    }

    @Test
    void listarHabitaciones_ShouldReturnList() throws Exception {
        when(habitacionService.listarHabitaciones()).thenReturn(List.of());

        mockMvc.perform(get("/reservas/habitacion"))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarHabitacion_ShouldReturnOk() throws Exception {
        CrearHabitacionDTO dto = new CrearHabitacionDTO(
                true, BigDecimal.valueOf(150), "Doble", 201, 1, 1, "admin123", "admin");

        when(habitacionService.actualizarHabitacion(any(CrearHabitacionDTO.class)))
                .thenReturn("Habitacion actualizada");

        mockMvc.perform(patch("/reservas/habitacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Habitacion actualizada"));
    }

    @Test
    void eliminarHabitacion_ShouldReturnOk_WhenValid() throws Exception {
        UserpassDTO userpass = new UserpassDTO("admin", "admin123");
        when(habitacionService.comprobarContrasena("admin", "admin123")).thenReturn(true);
        when(habitacionService.eliminarHabitacion(1)).thenReturn("habitacion eliminada");

        mockMvc.perform(delete("/reservas/habitacion/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userpass)))
                .andExpect(status().isOk())
                .andExpect(content().string("habitacion eliminada"));
    }

    @Test
    void eliminarHabitacion_ShouldReturnBadRequest_WhenInvalid() throws Exception {
        UserpassDTO userpass = new UserpassDTO("bad", "creds");
        when(habitacionService.comprobarContrasena("bad", "creds")).thenReturn(false);

        mockMvc.perform(delete("/reservas/habitacion/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userpass)))
                .andExpect(status().isBadRequest());
    }
}
