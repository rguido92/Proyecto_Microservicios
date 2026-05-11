package com.microservicios.usuarios.repositorio;

import com.microservicios.usuarios.entidades.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class IUserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    void findByNombre_ShouldReturnUser_WhenExists() {
        Usuario result = userRepository.findByNombre("admin");

        assertNotNull(result);
        assertEquals("admin", result.getNombre());
    }

    @Test
    void findByNombre_ShouldReturnNull_WhenNotExists() {
        assertNull(userRepository.findByNombre("nonexistent"));
    }

    @Test
    void findByNombreAndContrasena_ShouldReturnUser_WhenMatch() {
        Usuario result = userRepository.findByNombreAndContrasena("admin", "admin123");

        assertNotNull(result);
        assertEquals("ADMIN", result.getRol());
    }

    @Test
    void findByNombreAndContrasena_ShouldReturnNull_WhenNoMatch() {
        assertNull(userRepository.findByNombreAndContrasena("admin", "wrongpass"));
    }

    @Test
    void findAllByNombre_ShouldReturnList() {
        List<Usuario> result = userRepository.findAllByNombre("admin");

        assertEquals(1, result.size());
    }

    @Test
    void save_ShouldPersistUser() {
        Usuario usuario = new Usuario("newUser", "new@email.com", "New dir", "newpass", "USER");
        Usuario saved = userRepository.save(usuario);

        assertNotNull(saved);
        assertTrue(saved.getUsuario_id() > 0);
        assertEquals("newUser", saved.getNombre());
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        Optional<Usuario> result = userRepository.findById(1);

        assertTrue(result.isPresent());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Usuario> result = userRepository.findById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_ShouldRemoveUser() {
        Usuario usuario = new Usuario("deleteMe", "del@email.com", "Dir", "pass", "USER");
        usuario = userRepository.save(usuario);
        int id = usuario.getUsuario_id();

        userRepository.delete(usuario);

        assertFalse(userRepository.findById(id).isPresent());
    }
}
