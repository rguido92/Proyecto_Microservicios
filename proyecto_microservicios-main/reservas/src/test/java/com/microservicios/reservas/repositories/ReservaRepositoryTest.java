package com.microservicios.reservas.repositories;

import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.models.Hotel;
import com.microservicios.reservas.models.Reserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class ReservaRepositoryTest {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Autowired
    private IHotelRepository hotelRepository;

    private Habitacion habitacion;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel();
        hotel.setNombre("Test Hotel");
        hotel.setDireccion("Test Address");
        hotel = hotelRepository.save(hotel);

        habitacion = new Habitacion(0, hotel, 101, "Individual", BigDecimal.valueOf(100), true);
        habitacion = habitacionRepository.save(habitacion);
    }

    @Test
    void findByUsuario_ShouldReturnReservas() {
        Reserva r1 = new Reserva(0, 1, habitacion, LocalDate.now(), LocalDate.now().plusDays(1), "Pendiente");
        Reserva r2 = new Reserva(0, 1, habitacion, LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), "Confirmada");
        reservaRepository.save(r1);
        reservaRepository.save(r2);

        List<Reserva> result = reservaRepository.findByUsuario(1);

        assertEquals(2, result.size());
    }

    @Test
    void findByUsuario_ShouldReturnEmpty_WhenNoReservas() {
        List<Reserva> result = reservaRepository.findByUsuario(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByEstado_ShouldFilterByEstado() {
        reservaRepository.save(new Reserva(0, 1, habitacion, LocalDate.now(), LocalDate.now().plusDays(1), "Pendiente"));
        reservaRepository.save(new Reserva(0, 2, habitacion, LocalDate.now(), LocalDate.now().plusDays(1), "Confirmada"));

        List<Reserva> pendientes = reservaRepository.findByEstado("Pendiente");

        assertEquals(1, pendientes.size());
        assertEquals("Pendiente", pendientes.get(0).getEstado());
    }

    @Test
    void findcheckReserva_ShouldReturnReserva_WhenMatch() {
        Reserva saved = reservaRepository.save(
                new Reserva(0, 1, habitacion, LocalDate.now(), LocalDate.now().plusDays(1), "Pendiente"));

        Reserva result = reservaRepository.findcheckReserva(1, habitacion.getHotel().getHotelId(), saved.getId());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void findcheckReserva_ShouldReturnNull_WhenNoMatch() {
        Reserva result = reservaRepository.findcheckReserva(999, 999, 999);

        assertNull(result);
    }

    @Test
    void save_ShouldPersistReserva() {
        Reserva reserva = new Reserva(0, 5, habitacion, LocalDate.now(), LocalDate.now().plusDays(3), "Cancelada");

        Reserva saved = reservaRepository.save(reserva);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(5, saved.getUsuario());
    }

    @Test
    void findById_ShouldReturnReserva() {
        Reserva saved = reservaRepository.save(
                new Reserva(0, 1, habitacion, LocalDate.now(), LocalDate.now().plusDays(1), "Pendiente"));

        assertTrue(reservaRepository.findById(saved.getId()).isPresent());
    }
}
