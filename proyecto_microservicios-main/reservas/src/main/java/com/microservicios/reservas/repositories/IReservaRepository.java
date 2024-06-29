package com.microservicios.reservas.repositories;

import com.microservicios.reservas.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaRepository  extends JpaRepository<Reserva,Integer> {
    List<Reserva> findByUsuario(int usuario);

    List<Reserva> findByEstado(String estado);

    Reserva findById(int reserva_id);

    @Query("SELECT r FROM Reserva r " +
            "WHERE r.id = :idReserva " +
            "AND r.usuario = :idUsuario " +
            "AND r.habitacion.hotel.hotel_id = :idHotel")
    Reserva findcheckReserva(@Param("idUsuario") int idUsuario,@Param("idHotel") int idHotel,@Param("idReserva") int idReserva );
}
