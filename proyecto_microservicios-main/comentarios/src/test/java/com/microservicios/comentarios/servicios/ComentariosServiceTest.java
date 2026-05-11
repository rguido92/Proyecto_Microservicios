package com.microservicios.comentarios.servicios;

import com.microservicios.comentarios.dto.ListarComentariosHotelDTO;
import com.microservicios.comentarios.entidades.Comentario;
import com.microservicios.comentarios.repositorios.IComentariosRepository;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentariosServiceTest {

    @Mock
    private IComentariosRepository iComentariosRepository;

    @InjectMocks
    private ComentariosService comentariosService;

    @Test
    void eliminarComentario_ShouldDelete_WhenExists() {
        Comentario comentario = Comentario.builder().id("1").build();
        when(iComentariosRepository.findById("1")).thenReturn(Optional.of(comentario));

        String result = comentariosService.eliminarComentario("1");

        assertEquals("Se ha eliminado el comentario", result);
        verify(iComentariosRepository).deleteById("1");
    }

    @Test
    void eliminarComentario_ShouldReturnNotFound_WhenNotExists() {
        when(iComentariosRepository.findById("99")).thenReturn(Optional.empty());

        String result = comentariosService.eliminarComentario("99");

        assertEquals("La id del comentario no existe", result);
        verify(iComentariosRepository, never()).deleteById(any());
    }

    @Test
    void eliminarComentarios_ShouldDeleteAll() {
        doNothing().when(iComentariosRepository).deleteAll();

        String result = comentariosService.eliminarComentarios();

        assertEquals("Se han eliminado todos los comentarios", result);
    }

    @Test
    void eliminarComentarios_ShouldHandleException() {
        doThrow(new RuntimeException("Error")).when(iComentariosRepository).deleteAll();

        String result = comentariosService.eliminarComentarios();

        assertEquals("No se han podido eliminar los comentarios", result);
    }

    @Test
    void crearComentario_ShouldThrow_WhenInputNull() {
        assertThrows(IllegalArgumentException.class, () -> comentariosService.crearComentario(null));
    }

    @Test
    void existeComentario_ShouldReturnTrue_WhenExists() {
        Comentario comentario = Comentario.builder().id("1").build();
        when(iComentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(1, 1, 1))
                .thenReturn(comentario);

        assertTrue(comentariosService.existeComentario(1, 1, 1));
    }

    @Test
    void existeComentario_ShouldReturnFalse_WhenNotExists() {
        when(iComentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(1, 1, 1))
                .thenReturn(null);

        assertFalse(comentariosService.existeComentario(1, 1, 1));
    }

    @Test
    void puntuacionMediaHotel_ShouldReturnAverage() {
        Document doc = new Document("media", 7.5);
        AggregationResults<Document> results = mock(AggregationResults.class);
        when(results.getUniqueMappedResult()).thenReturn(doc);
        when(iComentariosRepository.puntuacionMediaHotel(1)).thenReturn(results);

        Double media = comentariosService.puntuacionMediaHotel(1);

        assertEquals(7.5, media);
    }

    @Test
    void puntuacionMediaHotel_ShouldReturnMinusOne_WhenNoData() {
        when(iComentariosRepository.puntuacionMediaHotel(1))
                .thenThrow(new NullPointerException());

        Double media = comentariosService.puntuacionMediaHotel(1);

        assertEquals(-1.0, media);
    }

    @Test
    void mediaPuntuacionPorUsuario_ShouldReturnAverage() {
        Document doc = new Document("media", 9.0);
        AggregationResults<Document> results = mock(AggregationResults.class);
        when(results.getUniqueMappedResult()).thenReturn(doc);
        when(iComentariosRepository.mediaUsuario(1)).thenReturn(results);

        Double media = comentariosService.mediaPuntuacionPorUsuario(1);

        assertEquals(9.0, media);
    }

    @Test
    void mediaPuntuacionPorUsuario_ShouldReturnMinusOne_WhenNoData() {
        when(iComentariosRepository.mediaUsuario(1))
                .thenThrow(new NullPointerException());

        Double media = comentariosService.mediaPuntuacionPorUsuario(1);

        assertEquals(-1.0, media);
    }

}
