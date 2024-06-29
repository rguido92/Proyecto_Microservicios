package com.microservicios.reservas.repositories;

import com.microservicios.reservas.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHotelRepository extends JpaRepository<Hotel,Integer> {

    Hotel findByNombre(String nombre);
}
