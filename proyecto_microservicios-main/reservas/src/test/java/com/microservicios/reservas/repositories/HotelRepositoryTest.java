package com.microservicios.reservas.repositories;

import com.microservicios.reservas.models.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class HotelRepositoryTest {

    @Autowired
    private IHotelRepository hotelRepository;

    @Test
    void findByNombre_ShouldReturnHotel_WhenExists() {
        Hotel hotel = new Hotel();
        hotel.setNombre("Test Hotel");
        hotel.setDireccion("Test Address");
        Hotel saved = hotelRepository.save(hotel);

        Hotel found = hotelRepository.findByNombre("Test Hotel");

        assertNotNull(found);
        assertEquals(saved.getHotelId(), found.getHotelId());
    }

    @Test
    void findByNombre_ShouldReturnNull_WhenNotExists() {
        assertNull(hotelRepository.findByNombre("Nonexistent Hotel"));
    }

    @Test
    void save_ShouldPersistHotel() {
        Hotel hotel = new Hotel();
        hotel.setNombre("New Hotel");
        hotel.setDireccion("New Address");

        Hotel saved = hotelRepository.save(hotel);

        assertNotNull(saved);
        assertTrue(saved.getHotelId() > 0);
    }

    @Test
    void findById_ShouldReturnHotel_WhenExists() {
        Hotel hotel = new Hotel();
        hotel.setNombre("Hotel For Id");
        hotel.setDireccion("Address");
        Hotel saved = hotelRepository.save(hotel);

        Optional<Hotel> found = hotelRepository.findById(saved.getHotelId());

        assertTrue(found.isPresent());
        assertEquals("Hotel For Id", found.get().getNombre());
    }

    @Test
    void delete_ShouldRemoveHotel() {
        Hotel hotel = new Hotel();
        hotel.setNombre("Delete Hotel");
        hotel.setDireccion("Address");
        Hotel saved = hotelRepository.save(hotel);

        hotelRepository.delete(saved);

        assertFalse(hotelRepository.findById(saved.getHotelId()).isPresent());
    }

    @Test
    void findAll_ShouldReturnAllHotels() {
        hotelRepository.save(new Hotel(0, "H1", "A1"));
        hotelRepository.save(new Hotel(0, "H2", "A2"));

        assertEquals(2, hotelRepository.findAll().size());
    }
}
