package com.microservicios.usuarios.servicio;


import com.microservicios.usuarios.dto.ActualizarUsuarioDTO;
import com.microservicios.usuarios.repositorio.IUserRepository;
import com.microservicios.usuarios.entidades.Usuario;
import com.microservicios.usuarios.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<String> saveUser(UsuarioDTO usuarioDTO) {
        if (userRepository.findByNombre(usuarioDTO.getNombre())==null){
            Usuario usuario = new Usuario();
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setCorreo_electronico(usuarioDTO.getCorreo_electronico());
            usuario.setDireccion(usuarioDTO.getDireccion());
            usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
            userRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("usuario guardado");
        }else  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuario duplicado");

    }

    @Transactional
    public String deleteUser(UsuarioDTO usuarioDTO) {
        Usuario usuario = userRepository.findByNombre(usuarioDTO.getNombre());
        if (usuario != null && passwordEncoder.matches(usuarioDTO.getContrasena(), usuario.getContrasena())) {
            userRepository.delete(usuario);
            return "Usuario eliminado correctamente";
        }
        return "El usuario no se encontro";
    }

    public UsuarioDTO findById(int id) {
        Usuario usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            return new UsuarioDTO(usuario);
        }
        return null;
    }

    public int findByNombre(String nombre) {
        Usuario usuario = userRepository.findByNombre(nombre);
        if (usuario != null) {
            return usuario.getUsuario_id();
        }
        return 0;
    }

    public List<UsuarioDTO> findAllUsers() {
        List<Usuario> usuarios = userRepository.findAll();
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            usuariosDTO.add(new UsuarioDTO(usuario));
        }
        return usuariosDTO;
    }

    public Boolean findByNombreAndContrasena(String nombre, String contrasena) {
        Usuario usuario = userRepository.findByNombre(nombre);
        if (usuario != null) {
            return passwordEncoder.matches(contrasena, usuario.getContrasena());
        }
        return false;
    }

    public UsuarioDTO getByNombreAndContrasena(String nombre, String contrasena) {
        Usuario usuario = userRepository.findByNombre(nombre);
        if (usuario != null && passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            return new UsuarioDTO(usuario);
        }
        return null;
    }

    @Transactional
    public ResponseEntity<String> actualizarUsuario(ActualizarUsuarioDTO usuarioDTO) {
        try {
            Usuario user = userRepository.findById(usuarioDTO.getId()).orElse(null);
            if (user != null) {
                user.setNombre(usuarioDTO.getNombre());
                user.setCorreo_electronico(usuarioDTO.getCorreo_electronico());
                user.setDireccion(usuarioDTO.getDireccion());
                user.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.CREATED).body("usuario actualizado");
            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("usuario no encontrado");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error: " + e.getMessage());
        }
    }

    public Boolean checkIfExists(int id) {
        Usuario usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            return true;
        } else
            return false;
    }
}

