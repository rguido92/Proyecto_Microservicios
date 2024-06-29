package com.microservicios.comentarios.repositorios;

import com.microservicios.comentarios.dto.ListarComentariosHotelDTO;
import com.microservicios.comentarios.entidades.Comentario;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface IComentariosRepository extends MongoRepository<Comentario, String> {
    Comentario findByHotelIdAndReservaIdAndUsuarioId(int hotelId, int reservaId, int usuarioId);
    List<Comentario> findByHotelId(int hotelId);
    List<Comentario>findByUsuarioId(int usuarioId);

    List<Comentario> findByUsuarioIdAndReservaId(int usuarioId, Integer reservaId);

    @Aggregation(pipeline = {"{$match: {hotelId: ?0}}","{$group: {_id: null, media: {$avg:\"$puntuacion\"}}}"})
    AggregationResults<Document> puntuacionMediaHotel(int idHotel);

    @Aggregation(pipeline = {"{$match: {usuarioId: ?0}}","{$group: {_id: null, media: {$avg:\"$puntuacion\"}}}"})
    AggregationResults<Document> mediaUsuario(int idUsuario);
}