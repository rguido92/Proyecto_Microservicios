package com.microservicios.usuarios.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.usuarios.dto.ActualizarUsuarioDTO;
import com.microservicios.usuarios.dto.UsuarioDTO;
import com.microservicios.usuarios.servicio.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearUsuario_ShouldReturn201_WhenSaved() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "testUser", "test@email.com", "dir", "pass", "USER");
        when(usuarioService.saveUser(any(UsuarioDTO.class)))
                .thenReturn(ResponseEntity.ok("usuario guardado"));

        mockMvc.perform(post("/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("usuario guardado"));
    }

    @Test
    void listarUsuarios_ShouldReturnList() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(1, "testUser", "test@email.com", "dir", "pass", "USER");
        when(usuarioService.findAllUsers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("testUser"));
    }

    @Test
    void eliminarUsuario_ShouldDelete_WhenValid() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "testUser", null, null, "pass", null);
        when(usuarioService.getByNombreAndContrasena("testUser", "pass"))
                .thenReturn(new UsuarioDTO(1, "testUser", null, null, "pass", null));

        mockMvc.perform(delete("/usuarios/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Usuario eliminado"));
    }

    @Test
    void eliminarUsuario_ShouldReturn400_WhenInvalid() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "testUser", null, null, "wrong", null);
        when(usuarioService.getByNombreAndContrasena("testUser", "wrong")).thenReturn(null);

        mockMvc.perform(delete("/usuarios/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuario no eliminado"));
    }

    @Test
    void actualizarUsuario_ShouldUpdate() throws Exception {
        ActualizarUsuarioDTO dto = new ActualizarUsuarioDTO(1, "updated", "e@e.com", "dir", "pass", "USER");
        when(usuarioService.actualizarUsuario(any(ActualizarUsuarioDTO.class)))
                .thenReturn(ResponseEntity.ok("usuario actualizado"));

        mockMvc.perform(put("/usuarios/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("usuario actualizado"));
    }

    @Test
    void validarUsuario_ShouldReturnTrue_WhenValid() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "testUser", null, null, "pass", null);
        when(usuarioService.findByNombreAndContrasena("testUser", "pass")).thenReturn(true);

        mockMvc.perform(post("/usuarios/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void login_ShouldReturnUser_WhenValid() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "testUser", null, null, "pass", null);
        UsuarioDTO responseDto = new UsuarioDTO(1, "testUser", "e@e.com", "dir", "pass", "USER");
        when(usuarioService.login("testUser", "pass")).thenReturn(responseDto);

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("testUser"));
    }

    @Test
    void login_ShouldReturn401_WhenInvalid() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(null, "testUser", null, null, "wrong", null);
        when(usuarioService.login("testUser", "wrong")).thenReturn(null);

        mockMvc.perform(post("/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNombreUsuario_ShouldReturnName() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(1, "testUser", null, null, null, null);
        when(usuarioService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/usuarios/info/id/").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("El nombre del usuario con id : 1 es testUser"));
    }

    @Test
    void getNombreUsuario_ShouldReturn404_WhenNotFound() throws Exception {
        when(usuarioService.findById(99)).thenReturn(null);

        mockMvc.perform(get("/usuarios/info/id/").param("id", "99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado "));
    }

    @Test
    void getIdUsuario_ShouldReturnId() throws Exception {
        when(usuarioService.findByNombre("testUser")).thenReturn(1);

        mockMvc.perform(get("/usuarios/info/nombre/").param("nombre", "testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void getIdUsuario_ShouldReturn404_WhenNotFound() throws Exception {
        when(usuarioService.findByNombre("unknown")).thenReturn(0);

        mockMvc.perform(get("/usuarios/info/nombre/").param("nombre", "unknown"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    @Test
    void checkIfExist_ShouldReturnTrue_WhenExists() throws Exception {
        when(usuarioService.checkIfExists(1)).thenReturn(true);

        mockMvc.perform(get("/usuarios/checkIfExist/").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void checkIfExist_ShouldReturn404_WhenNotExists() throws Exception {
        when(usuarioService.checkIfExists(99)).thenReturn(false);

        mockMvc.perform(get("/usuarios/checkIfExist/").param("id", "99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("false"));
    }
}
