package com.microservicios.reservas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.reservas.dto.HotelDTO;
import com.microservicios.reservas.dto.UserpassDTO;
import com.microservicios.reservas.services.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearHotel_ShouldReturnOk_WhenValid() throws Exception {
        HotelDTO dto = new HotelDTO("admin", "admin123", "New Hotel", "Address");
        when(hotelService.comprobarContrasena("admin", "admin123")).thenReturn(true);
        when(hotelService.crearHotel(any(HotelDTO.class))).thenReturn("Hotel creado correctamente");

        mockMvc.perform(post("/reservas/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Hotel creado correctamente"));
    }

    @Test
    void crearHotel_ShouldReturnUnauthorized_WhenInvalid() throws Exception {
        HotelDTO dto = new HotelDTO("bad", "creds", "Hotel", "Addr");
        when(hotelService.comprobarContrasena("bad", "creds")).thenReturn(false);

        mockMvc.perform(post("/reservas/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listarHoteles_ShouldReturnList() throws Exception {
        when(hotelService.listarHoteles()).thenReturn(List.of(new HotelDTO("Hotel1", "Addr1")));

        mockMvc.perform(get("/reservas/hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Hotel1"));
    }

    @Test
    void actualizarHotel_ShouldReturnOk() throws Exception {
        HotelDTO dto = new HotelDTO(1, "admin", "admin123", "Updated", "Addr");
        when(hotelService.comprobarContrasena("admin", "admin123")).thenReturn(true);
        when(hotelService.actualizarHotel(any(HotelDTO.class))).thenReturn("Hotel actualizado correctamente");

        mockMvc.perform(patch("/reservas/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Hotel actualizado correctamente"));
    }

    @Test
    void eliminarHotel_ShouldReturnOk() throws Exception {
        UserpassDTO userpass = new UserpassDTO("admin", "admin123");
        when(hotelService.comprobarContrasena("admin", "admin123")).thenReturn(true);
        when(hotelService.eliminarHotel(1)).thenReturn("Hotel eliminado correctamente");

        mockMvc.perform(delete("/reservas/hotel/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userpass)))
                .andExpect(status().isOk())
                .andExpect(content().string("Hotel eliminado correctamente"));
    }

    @Test
    void obtenerIdApartirNombre_ShouldReturnId() throws Exception {
        UserpassDTO userpass = new UserpassDTO("admin", "admin123");
        when(hotelService.comprobarContrasena("admin", "admin123")).thenReturn(true);
        when(hotelService.obtenerIdApartirNombre("TestHotel")).thenReturn(5);

        mockMvc.perform(post("/reservas/hotel/id")
                        .param("nombre", "TestHotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userpass)))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void obtenerNombreAPartirId_ShouldReturnName() throws Exception {
        UserpassDTO userpass = new UserpassDTO("admin", "admin123");
        when(hotelService.comprobarContrasena("admin", "admin123")).thenReturn(true);
        when(hotelService.obtenerNombreAPartirId(1)).thenReturn("Nombre del hotel con ID 1: TestHotel");

        mockMvc.perform(post("/reservas/hotel/nombre")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userpass)))
                .andExpect(status().isOk())
                .andExpect(content().string("Nombre del hotel con ID 1: TestHotel"));
    }
}
