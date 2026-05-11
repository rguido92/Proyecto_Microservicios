package com.microservicios.reservas.services;

import com.microservicios.reservas.dto.CheckReservaDTO;
import com.microservicios.reservas.dto.ListarReservasDTO;
import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import com.microservicios.reservas.models.Reserva;
import com.microservicios.reservas.repositories.IReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private IReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reserva;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(reservaService, "usuariosServiceUrl", "http://localhost:8702");
        Hotel hotel = new Hotel(1, "Test Hotel", "Test Address");
        Habitacion habitacion = new Habitacion(1, hotel, 101, "Individual", BigDecimal.valueOf(100), true);
        reserva = new Reserva(1, 1, habitacion, LocalDate.now(), LocalDate.now().plusDays(2), "Pendiente");
    }

    @Test
    void findbyEstado_ShouldReturnFilteredReservas() {
        when(reservaRepository.findByEstado("Pendiente")).thenReturn(List.of(reserva));

        List<ListarReservasDTO> result = reservaService.findbyEstado("Pendiente");

        assertEquals(1, result.size());
    }

    @Test
    void findbyEstado_ShouldReturnEmpty_WhenNoMatch() {
        when(reservaRepository.findByEstado("Cancelada")).thenReturn(List.of());

        List<ListarReservasDTO> result = reservaService.findbyEstado("Cancelada");

        assertTrue(result.isEmpty());
    }

    @Test
    void checkReserva_ShouldReturnTrue_WhenExists() {
        when(reservaRepository.findcheckReserva(1, 1, 1)).thenReturn(reserva);

        CheckReservaDTO dto = new CheckReservaDTO();
        dto.setIdUsuario(1);
        dto.setIdHotel(1);
        dto.setIdReserva(1);

        assertTrue(reservaService.checkReserva(dto));
    }

    @Test
    void checkReserva_ShouldReturnFalse_WhenNotExists() {
        when(reservaRepository.findcheckReserva(99, 99, 99)).thenReturn(null);

        CheckReservaDTO dto = new CheckReservaDTO();
        dto.setIdUsuario(99);
        dto.setIdHotel(99);
        dto.setIdReserva(99);

        assertFalse(reservaService.checkReserva(dto));
    }
}
