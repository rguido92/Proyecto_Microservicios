package com.microservicios.comentarios.repositorios;

import com.microservicios.comentarios.entidades.Comentario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IComentariosRepositoryTest {

    @Mock
    private IComentariosRepository comentariosRepository;

    @Test
    void findByHotelIdAndReservaIdAndUsuarioId_ShouldReturnComentario_WhenExists() {
        Comentario comentario = Comentario.builder()
                .id("1")
                .hotelId(1)
                .reservaId(1)
                .usuarioId(1)
                .build();

        when(comentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(1, 1, 1))
                .thenReturn(comentario);

        Comentario result = comentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(1, 1, 1);

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void findByHotelIdAndReservaIdAndUsuarioId_ShouldReturnNull_WhenNotExists() {
        when(comentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(99, 99, 99))
                .thenReturn(null);

        assertNull(comentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(99, 99, 99));
    }

    @Test
    void findByHotelId_ShouldReturnList() {
        Comentario c1 = Comentario.builder().id("1").hotelId(1).build();
        Comentario c2 = Comentario.builder().id("2").hotelId(1).build();
        when(comentariosRepository.findByHotelId(1)).thenReturn(List.of(c1, c2));

        List<Comentario> result = comentariosRepository.findByHotelId(1);

        assertEquals(2, result.size());
    }

    @Test
    void findByUsuarioId_ShouldReturnList() {
        Comentario c = Comentario.builder().id("1").usuarioId(1).build();
        when(comentariosRepository.findByUsuarioId(1)).thenReturn(List.of(c));

        List<Comentario> result = comentariosRepository.findByUsuarioId(1);

        assertEquals(1, result.size());
    }

    @Test
    void findByUsuarioIdAndReservaId_ShouldReturnList() {
        Comentario c = Comentario.builder().id("1").usuarioId(1).reservaId(1).build();
        when(comentariosRepository.findByUsuarioIdAndReservaId(1, 1)).thenReturn(List.of(c));

        List<Comentario> result = comentariosRepository.findByUsuarioIdAndReservaId(1, 1);

        assertEquals(1, result.size());
    }

    @Test
    void findById_ShouldReturnComentario() {
        Comentario c = Comentario.builder().id("abc123").build();
        when(comentariosRepository.findById("abc123")).thenReturn(Optional.of(c));

        Optional<Comentario> result = comentariosRepository.findById("abc123");

        assertTrue(result.isPresent());
    }

    @Test
    void save_ShouldPersistComentario() {
        Comentario toSave = Comentario.builder()
                .usuarioId(1)
                .hotelId(1)
                .reservaId(1)
                .puntuacion(7.5)
                .comentario("Good")
                .fechaCreacion(LocalDateTime.now())
                .build();

        Comentario saved = Comentario.builder()
                .id("new123")
                .usuarioId(1)
                .hotelId(1)
                .reservaId(1)
                .puntuacion(7.5)
                .comentario("Good")
                .build();

        when(comentariosRepository.save(any(Comentario.class))).thenReturn(saved);

        Comentario result = comentariosRepository.save(toSave);

        assertNotNull(result);
        assertEquals("new123", result.getId());
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(comentariosRepository).deleteById("1");

        comentariosRepository.deleteById("1");

        verify(comentariosRepository).deleteById("1");
    }

    @Test
    void findAll_ShouldReturnAll() {
        when(comentariosRepository.findAll()).thenReturn(List.of(
                Comentario.builder().id("1").build(),
                Comentario.builder().id("2").build()
        ));

        List<Comentario> result = comentariosRepository.findAll();

        assertEquals(2, result.size());
    }
}
