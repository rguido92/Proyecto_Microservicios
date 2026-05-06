package com.microservicios.reservas.repositories;

import com.microservicios.reservas.models.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface IHabitacionRepository extends JpaRepository<Habitacion, Integer> {

    @Query("SELECT h FROM Habitacion h WHERE h.hotel.hotel_id = :hotelId")
    List<Habitacion> findByHotelId(@Param("hotelId") int hotelId);
}
 