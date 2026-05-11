package com.microservicios.reservas.services;

import com.microservicios.reservas.dto.HotelDTO;
import com.microservicios.reservas.models.Hotel;
import com.microservicios.reservas.repositories.IHotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private IHotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private HotelDTO hotelDTO;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(hotelService, "usuariosServiceUrl", "http://localhost:8702");

        hotel = new Hotel(1, "Test Hotel", "Test Address");
        hotelDTO = new HotelDTO();
        hotelDTO.setNombre("Test Hotel");
        hotelDTO.setDireccion("Test Address");
        hotelDTO.setUsuario("admin");
        hotelDTO.setContrasena("admin123");
    }

    @Test
    void crearHotel_ShouldCreateSuccessfully() {
        String result = hotelService.crearHotel(hotelDTO);

        assertEquals("Hotel creado correctamente", result);
        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void crearHotel_ShouldHandleException() {
        when(hotelRepository.save(any(Hotel.class))).thenThrow(new RuntimeException("DB error"));

        String result = hotelService.crearHotel(hotelDTO);

        assertTrue(result.contains("Error al crear el hotel"));
    }

    @Test
    void actualizarHotel_ShouldUpdate_WhenExists() {
        HotelDTO updateDTO = new HotelDTO();
        updateDTO.setHotelId(1);
        updateDTO.setNombre("Updated Hotel");
        updateDTO.setDireccion("Updated Address");

        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));

        String result = hotelService.actualizarHotel(updateDTO);

        assertEquals("Hotel actualizado correctamente", result);
        assertEquals("Updated Hotel", hotel.getNombre());
        verify(hotelRepository).save(hotel);
    }

    @Test
    void actualizarHotel_ShouldReturnNotFound_WhenNotExists() {
        when(hotelRepository.findById(99)).thenReturn(Optional.empty());

        HotelDTO updateDTO = new HotelDTO();
        updateDTO.setHotelId(99);

        String result = hotelService.actualizarHotel(updateDTO);

        assertEquals("Hotel no encontrado", result);
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void eliminarHotel_ShouldDelete_WhenExists() {
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));

        String result = hotelService.eliminarHotel(1);

        assertEquals("Hotel eliminado correctamente", result);
        verify(hotelRepository).delete(hotel);
    }

    @Test
    void eliminarHotel_ShouldReturnNotFound_WhenNotExists() {
        when(hotelRepository.findById(99)).thenReturn(Optional.empty());

        String result = hotelService.eliminarHotel(99);

        assertEquals("Hotel no encontrado", result);
        verify(hotelRepository, never()).delete(any());
    }

    @Test
    void obtenerIdApartirNombre_ShouldReturnId_WhenExists() {
        when(hotelRepository.findByNombre("Test Hotel")).thenReturn(hotel);

        int id = hotelService.obtenerIdApartirNombre("Test Hotel");

        assertEquals(1, id);
    }

    @Test
    void obtenerIdApartirNombre_ShouldReturnZero_WhenNotExists() {
        when(hotelRepository.findByNombre("Unknown")).thenReturn(null);

        assertEquals(0, hotelService.obtenerIdApartirNombre("Unknown"));
    }

    @Test
    void obtenerNombreAPartirId_ShouldReturnName_WhenExists() {
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));

        String result = hotelService.obtenerNombreAPartirId(1);

        assertEquals("Nombre del hotel con ID 1: Test Hotel", result);
    }

    @Test
    void obtenerNombreAPartirId_ShouldReturnNotFound_WhenNotExists() {
        when(hotelRepository.findById(99)).thenReturn(Optional.empty());

        String result = hotelService.obtenerNombreAPartirId(99);

        assertEquals("Hotel no encontrado con ID 99", result);
    }

    @Test
    void listarHoteles_ShouldReturnAllHoteles() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        List<HotelDTO> result = hotelService.listarHoteles();

        assertEquals(1, result.size());
        assertEquals("Test Hotel", result.get(0).getNombre());
    }
}
