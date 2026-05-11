package com.microservicios.reservas.services;

import com.microservicios.reservas.dto.HabitacionDTO;
import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import com.microservicios.reservas.repositories.IHabitacionRepository;
import com.microservicios.reservas.repositories.IHotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceTest {

    @Mock
    private IHabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionService habitacionService;

    private Habitacion habitacion;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel(1, "Test Hotel", "Test Address");
        habitacion = new Habitacion(1, hotel, 101, "Individual", BigDecimal.valueOf(100), true);
    }

    @Test
    void eliminarHabitacion_ShouldDelete_WhenExists() {
        when(habitacionRepository.findById(1)).thenReturn(Optional.of(habitacion));

        String result = habitacionService.eliminarHabitacion(1);

        assertEquals("habitacion eliminada", result);
        verify(habitacionRepository).delete(habitacion);
    }

    @Test
    void eliminarHabitacion_ShouldReturnInvalid_WhenNotExists() {
        when(habitacionRepository.findById(99)).thenReturn(Optional.empty());

        String result = habitacionService.eliminarHabitacion(99);

        assertEquals("el id no es valido", result);
        verify(habitacionRepository, never()).delete(any());
    }

    @Test
    void obtenerHabitacionPorId_ShouldReturnDTO_WhenExists() {
        when(habitacionRepository.findById(1)).thenReturn(Optional.of(habitacion));

        HabitacionDTO result = habitacionService.obtenerHabitacionPorId(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Individual", result.getTipo());
    }

    @Test
    void obtenerHabitacionPorId_ShouldReturnNull_WhenNotExists() {
        when(habitacionRepository.findById(99)).thenReturn(Optional.empty());

        assertNull(habitacionService.obtenerHabitacionPorId(99));
    }

    @Test
    void findById_ShouldReturnEntity_WhenExists() {
        when(habitacionRepository.findById(1)).thenReturn(Optional.of(habitacion));

        Habitacion result = habitacionService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void findById_ShouldReturnNull_WhenNotExists() {
        when(habitacionRepository.findById(99)).thenReturn(Optional.empty());

        assertNull(habitacionService.findById(99));
    }

    @Test
    void listarHabitaciones_ShouldReturnAll() {
        when(habitacionRepository.findAll()).thenReturn(List.of(habitacion));

        List<HabitacionDTO> result = habitacionService.listarHabitaciones();

        assertEquals(1, result.size());
    }
}
