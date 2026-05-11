package com.microservicios.comentarios.controladores;

import com.microservicios.comentarios.dto.*;
import com.microservicios.comentarios.servicios.ComentariosService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentariosControllerTest {

    @Mock
    private ComentariosService comentariosService;

    @InjectMocks
    private ComentariosController comentariosController;

    @Test
    void crearComentario_ShouldReturnDTO_WhenValid() {
        CrearComentarioDTO input = CrearComentarioDTO.builder()
                .nombreUsuario("testUser")
                .contrasena("pass")
                .nombreHotel("Hotel")
                .reservaId(1)
                .puntuacion(8)
                .comentario("Good")
                .build();

        when(comentariosService.crearComentario(any(CrearComentarioDTO.class))).thenReturn(input);

        CrearComentarioDTO result = comentariosController.crearComentario(input);

        assertNotNull(result);
        assertEquals("testUser", result.getNombreUsuario());
    }

    @Test
    void crearComentario_ShouldThrow_WhenInputNull() {
        assertThrows(IllegalArgumentException.class, () -> comentariosController.crearComentario(null));
    }

    @Test
    void eliminarComentarios_ShouldReturnMessage() {
        when(comentariosService.eliminarComentarios()).thenReturn("Se han eliminado todos los comentarios");

        String result = comentariosController.eliminarComentarios();

        assertEquals("Se han eliminado todos los comentarios", result);
    }

    @Test
    void eliminarComentarioDTO_ShouldDelete_WhenValidUser() {
        EliminarComentarioDTO dto = new EliminarComentarioDTO("testUser", "pass", "1");
        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(true);
        when(comentariosService.eliminarComentario("1")).thenReturn("Se ha eliminado el comentario");

        String result = comentariosController.eliminarComentarioDTO(dto);

        assertEquals("Se ha eliminado el comentario", result);
    }

    @Test
    void eliminarComentarioDTO_ShouldReturnError_WhenInvalidUser() {
        EliminarComentarioDTO dto = new EliminarComentarioDTO("bad", "creds", "1");
        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(false);

        String result = comentariosController.eliminarComentarioDTO(dto);

        assertEquals("Error al autenticarse", result);
        verify(comentariosService, never()).eliminarComentario(anyString());
    }

    @Test
    void listarComentariosHotel_ShouldReturnList_WhenValid() {
        ListarComentariosHotelDTO input = new ListarComentariosHotelDTO("testUser", "pass", "Hotel");
        ListarComentariosHotelDTO output = new ListarComentariosHotelDTO();

        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(true);
        when(comentariosService.listarComentarioHotel(any(ListarComentariosHotelDTO.class)))
                .thenReturn(List.of(output));

        List<ListarComentariosHotelDTO> result = comentariosController.listarComentariosHotel(input);

        assertEquals(1, result.size());
    }

    @Test
    void listarComentariosHotel_ShouldReturnEmpty_WhenInvalid() {
        ListarComentariosHotelDTO input = new ListarComentariosHotelDTO("bad", "creds", "Hotel");
        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(false);

        List<ListarComentariosHotelDTO> result = comentariosController.listarComentariosHotel(input);

        assertTrue(result.isEmpty());
    }

    @Test
    void listarComentariosUsuario_ShouldReturnList_WhenValid() {
        UserPassDTO userPass = new UserPassDTO("testUser", "pass");
        when(comentariosService.validarUsuario(userPass)).thenReturn(true);
        when(comentariosService.listarComentarioUsuario(userPass)).thenReturn(List.of(new ListarComentariosHotelDTO()));

        List<ListarComentariosHotelDTO> result = comentariosController.listarComentariosUsuario(userPass);

        assertEquals(1, result.size());
    }

    @Test
    void listarComentariosUsuario_ShouldReturnEmpty_WhenInvalid() {
        UserPassDTO userPass = new UserPassDTO("bad", "creds");
        when(comentariosService.validarUsuario(userPass)).thenReturn(false);

        List<ListarComentariosHotelDTO> result = comentariosController.listarComentariosUsuario(userPass);

        assertTrue(result.isEmpty());
    }

    @Test
    void puntuacionMediaHotel_ShouldReturnScore_WhenValid() {
        ObtenerHotelDTO dto = new ObtenerHotelDTO("Hotel", "testUser", "pass");
        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(true);
        when(comentariosService.obtenerIdApartirNombre(eq("Hotel"), any(UserPassDTO.class))).thenReturn(1);
        when(comentariosService.puntuacionMediaHotel(1)).thenReturn(8.5);

        Double result = comentariosController.puntuacionMediaHotel(dto);

        assertEquals(8.5, result);
    }

    @Test
    void puntuacionMediaHotel_ShouldReturnMinusOne_WhenInvalid() {
        ObtenerHotelDTO dto = new ObtenerHotelDTO("Hotel", "bad", "creds");
        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(false);

        Double result = comentariosController.puntuacionMediaHotel(dto);

        assertEquals(-1.0, result);
    }

    @Test
    void mostrarComentarioUsuarioReserva_ShouldReturnList_WhenValid() {
        MostrarComentarioReservaDTO dto = new MostrarComentarioReservaDTO("testUser", "pass", 1);
        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(true);
        when(comentariosService.mostrarComentarioReservaUsuario(dto))
                .thenReturn(List.of(new ListarComentariosHotelDTO()));

        List<ListarComentariosHotelDTO> result = comentariosController.mostrarComentarioUsuarioReserva(dto);

        assertEquals(1, result.size());
    }

    @Test
    void mostrarComentarioUsuarioReserva_ShouldReturnEmpty_WhenInvalid() {
        MostrarComentarioReservaDTO dto = new MostrarComentarioReservaDTO("bad", "creds", 1);
        when(comentariosService.validarUsuario(any(UserPassDTO.class))).thenReturn(false);

        List<ListarComentariosHotelDTO> result = comentariosController.mostrarComentarioUsuarioReserva(dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void puntuacionesMediasUsuario_ShouldReturnScore_WhenValid() {
        UserPassDTO userPass = new UserPassDTO("testUser", "pass");
        when(comentariosService.validarUsuario(userPass)).thenReturn(true);
        when(comentariosService.obtenerIdUsuario("testUser")).thenReturn(1);
        when(comentariosService.mediaPuntuacionPorUsuario(1)).thenReturn(9.0);

        Double result = comentariosController.puntuacionesMediasUsuario(userPass);

        assertEquals(9.0, result);
    }

    @Test
    void puntuacionesMediasUsuario_ShouldReturnNull_WhenInvalid() {
        UserPassDTO userPass = new UserPassDTO("bad", "creds");
        when(comentariosService.validarUsuario(userPass)).thenReturn(false);

        assertNull(comentariosController.puntuacionesMediasUsuario(userPass));
    }
}
