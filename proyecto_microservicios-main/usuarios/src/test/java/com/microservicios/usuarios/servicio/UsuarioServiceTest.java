package com.microservicios.usuarios.servicio;

import com.microservicios.usuarios.dto.ActualizarUsuarioDTO;
import com.microservicios.usuarios.dto.UsuarioDTO;
import com.microservicios.usuarios.entidades.Usuario;
import com.microservicios.usuarios.repositorio.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("testUser", "test@email.com", "Calle 123", "pass123", "USER");
        usuario.setUsuario_id(1);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("testUser");
        usuarioDTO.setCorreo_electronico("test@email.com");
        usuarioDTO.setDireccion("Calle 123");
        usuarioDTO.setContrasena("pass123");
        usuarioDTO.setRol("USER");
    }

    @Test
    void saveUser_ShouldCreateUser_WhenNameNotExists() {
        when(userRepository.findByNombre("testUser")).thenReturn(null);

        ResponseEntity<String> response = usuarioService.saveUser(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("usuario guardado", response.getBody());
        verify(userRepository).save(any(Usuario.class));
    }

    @Test
    void saveUser_ShouldReturnDuplicate_WhenNameExists() {
        when(userRepository.findByNombre("testUser")).thenReturn(usuario);

        ResponseEntity<String> response = usuarioService.saveUser(usuarioDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("usuario duplicado", response.getBody());
        verify(userRepository, never()).save(any());
    }

    @Test
    void saveUser_ShouldSetDefaultRol_WhenRolIsBlank() {
        usuarioDTO.setRol("");
        when(userRepository.findByNombre("testUser")).thenReturn(null);

        usuarioService.saveUser(usuarioDTO);

        verify(userRepository).save(argThat(u -> "USER".equals(u.getRol())));
    }

    @Test
    void deleteUser_ShouldDelete_WhenCredentialsMatch() {
        when(userRepository.findByNombreAndContrasena("testUser", "pass123")).thenReturn(usuario);

        String result = usuarioService.deleteUser(usuarioDTO);

        assertEquals("Usuario eliminado correctamente", result);
        verify(userRepository).delete(usuario);
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenCredentialsDoNotMatch() {
        when(userRepository.findByNombreAndContrasena("testUser", "wrong")).thenReturn(null);

        usuarioDTO.setContrasena("wrong");
        String result = usuarioService.deleteUser(usuarioDTO);

        assertEquals("El usuario no se encontro", result);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void findById_ShouldReturnDTO_WhenExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioDTO result = usuarioService.findById(1);

        assertNotNull(result);
        assertEquals("testUser", result.getNombre());
    }

    @Test
    void findById_ShouldReturnNull_WhenNotExists() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertNull(usuarioService.findById(99));
    }

    @Test
    void findByNombre_ShouldReturnId_WhenExists() {
        when(userRepository.findByNombre("testUser")).thenReturn(usuario);

        int id = usuarioService.findByNombre("testUser");

        assertEquals(1, id);
    }

    @Test
    void findByNombre_ShouldReturnZero_WhenNotExists() {
        when(userRepository.findByNombre("unknown")).thenReturn(null);

        assertEquals(0, usuarioService.findByNombre("unknown"));
    }

    @Test
    void findAllUsers_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioDTO> result = usuarioService.findAllUsers();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getNombre());
    }

    @Test
    void findByNombreAndContrasena_ShouldReturnTrue_WhenMatch() {
        when(userRepository.findByNombreAndContrasena("testUser", "pass123")).thenReturn(usuario);

        assertTrue(usuarioService.findByNombreAndContrasena("testUser", "pass123"));
    }

    @Test
    void findByNombreAndContrasena_ShouldReturnFalse_WhenNoMatch() {
        when(userRepository.findByNombreAndContrasena("testUser", "wrong")).thenReturn(null);

        assertFalse(usuarioService.findByNombreAndContrasena("testUser", "wrong"));
    }

    @Test
    void getByNombreAndContrasena_ShouldReturnDTO_WhenMatch() {
        when(userRepository.findByNombreAndContrasena("testUser", "pass123")).thenReturn(usuario);

        UsuarioDTO result = usuarioService.getByNombreAndContrasena("testUser", "pass123");

        assertNotNull(result);
        assertEquals("testUser", result.getNombre());
    }

    @Test
    void getByNombreAndContrasena_ShouldReturnNull_WhenNoMatch() {
        when(userRepository.findByNombreAndContrasena("testUser", "wrong")).thenReturn(null);

        assertNull(usuarioService.getByNombreAndContrasena("testUser", "wrong"));
    }

    @Test
    void login_ShouldReturnDTO_WhenCredentialsValid() {
        when(userRepository.findByNombreAndContrasena("testUser", "pass123")).thenReturn(usuario);

        UsuarioDTO result = usuarioService.login("testUser", "pass123");

        assertNotNull(result);
        assertEquals("testUser", result.getNombre());
    }

    @Test
    void login_ShouldReturnNull_WhenCredentialsInvalid() {
        when(userRepository.findByNombreAndContrasena("testUser", "wrong")).thenReturn(null);

        assertNull(usuarioService.login("testUser", "wrong"));
    }

    @Test
    void actualizarUsuario_ShouldUpdate_WhenUserExists() {
        ActualizarUsuarioDTO updateDTO = new ActualizarUsuarioDTO();
        updateDTO.setId(1);
        updateDTO.setNombre("updatedUser");
        updateDTO.setCorreo_electronico("updated@email.com");
        updateDTO.setDireccion("New Address");
        updateDTO.setContrasena("newPass");
        updateDTO.setRol("ADMIN");

        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        ResponseEntity<String> response = usuarioService.actualizarUsuario(updateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("usuario actualizado", response.getBody());
        assertEquals("updatedUser", usuario.getNombre());
        verify(userRepository).save(usuario);
    }

    @Test
    void actualizarUsuario_ShouldReturnBadRequest_WhenUserNotFound() {
        ActualizarUsuarioDTO updateDTO = new ActualizarUsuarioDTO();
        updateDTO.setId(99);

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<String> response = usuarioService.actualizarUsuario(updateDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("usuario no actualizado", response.getBody());
    }

    @Test
    void checkIfExists_ShouldReturnTrue_WhenExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(usuario));

        assertTrue(usuarioService.checkIfExists(1));
    }

    @Test
    void checkIfExists_ShouldReturnFalse_WhenNotExists() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertFalse(usuarioService.checkIfExists(99));
    }
}
