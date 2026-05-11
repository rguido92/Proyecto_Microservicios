package com.microservicios.reservas.repositories;

import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class HabitacionRepositoryTest {

    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Autowired
    private IHotelRepository hotelRepository;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setNombre("Test Hotel");
        hotel.setDireccion("Test Address");
        hotel = hotelRepository.save(hotel);
    }

    @Test
    void findByHotelId_ShouldReturnHabitaciones() {
        Habitacion h1 = new Habitacion(0, hotel, 101, "Individual", BigDecimal.valueOf(100), true);
        Habitacion h2 = new Habitacion(0, hotel, 102, "Doble", BigDecimal.valueOf(150), true);
        habitacionRepository.save(h1);
        habitacionRepository.save(h2);

        List<Habitacion> result = habitacionRepository.findByHotelId(hotel.getHotelId());

        assertEquals(2, result.size());
    }

    @Test
    void findByHotelId_ShouldReturnEmpty_WhenNoHabitaciones() {
        List<Habitacion> result = habitacionRepository.findByHotelId(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_ShouldPersistHabitacion() {
        Habitacion habitacion = new Habitacion(0, hotel, 201, "Suite", BigDecimal.valueOf(300), true);

        Habitacion saved = habitacionRepository.save(habitacion);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(201, saved.getNumero_habitacion());
    }

    @Test
    void findById_ShouldReturnHabitacion_WhenExists() {
        Habitacion habitacion = new Habitacion(0, hotel, 301, "Triple", BigDecimal.valueOf(200), true);
        Habitacion saved = habitacionRepository.save(habitacion);

        Optional<Habitacion> found = habitacionRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Triple", found.get().getTipo());
    }

    @Test
    void delete_ShouldRemoveHabitacion() {
        Habitacion habitacion = new Habitacion(0, hotel, 401, "Individual", BigDecimal.valueOf(80), false);
        Habitacion saved = habitacionRepository.save(habitacion);

        habitacionRepository.delete(saved);

        assertFalse(habitacionRepository.findById(saved.getId()).isPresent());
    }
}
