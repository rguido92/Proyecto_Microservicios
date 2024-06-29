package com.microservicios.usuarios.controlador;

import com.microservicios.usuarios.dto.ActualizarUsuarioDTO;
import com.microservicios.usuarios.dto.UsuarioDTO;
import com.microservicios.usuarios.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private  UsuarioService usuarioService;

    /**
     * @param usuario
     * @return
     */
    @PostMapping("/registrar")
    public ResponseEntity<String> crearUsuario(@RequestBody UsuarioDTO usuario) {
            return usuarioService.saveUser(usuario);
    }

    /**
     * http://localhost:8702/usuarios/remove?id=4
     * http://localhost:8702/usuarios/?nombre=María López&contrasena=secreto456
     *
     * @param usuario
     * @return
     */
    @DeleteMapping("/")
    public ResponseEntity<String> eliminarUsuario(@RequestBody UsuarioDTO usuario) {
        UsuarioDTO user = usuarioService.getByNombreAndContrasena(usuario.getNombre(), usuario.getContrasena());
        if (user != null) {
            usuarioService.deleteUser(usuario);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Usuario eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario no eliminado");
        }
    }

    /**
     * http://localhost:8702/usuarios/registrar
     * {
     * "usuario_id":8,
     * "nombre": "Paco Editado",
     * "correo_electronico": "edit@example.com",
     * "direccion": "Dirección del editad",
     * "contrasena": "editapass"
     * }
     *
     * @param usuarioDTO
     * @return
     */
    @PutMapping("/registrar")
    public ResponseEntity<String> actualizarUsuario(@RequestBody ActualizarUsuarioDTO usuarioDTO) {
         return   usuarioService.actualizarUsuario(usuarioDTO);

    }

    /**
     * http://localhost:8702/usuarios/validar
     * {
     * "nombre": "Rodri",
     * "contrasena": "password789"
     * }
     *
     * @param usuarioDTO
     * @return
     */
    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        boolean validado = usuarioService.findByNombreAndContrasena(usuarioDTO.getNombre(), usuarioDTO.getContrasena());
        return ResponseEntity.ok(validado);
    }

    // http://localhost:8702/usuarios/info/id/?id=1
    @GetMapping("/info/id/")
    public ResponseEntity<String> getNombreUsuario(@RequestParam int id) {
        UsuarioDTO usuario = usuarioService.findById(id);
        if (usuario != null) {
            return ResponseEntity.ok("El nombre del usuario con id : " + id + " es " + usuario.getNombre());

        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado ");
    }

    /**
     * no debe existir un nombre repetido ?
     *
     * @param nombre
     * @return
     */
    @GetMapping("/info/nombre/")
    public ResponseEntity<Integer> getIdUsuario(@RequestParam String nombre) {
        int idUsuario = usuarioService.findByNombre(nombre);
        if (idUsuario != 0) {
            return ResponseEntity.ok(idUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
    }

    /**
     * http://localhost:8702/usuarios/checkIfExist/?id=10
     *
     * @param id
     * @return
     */
    @GetMapping("/checkIfExist/")
    public ResponseEntity<String> checkIfExist(@RequestParam int id) {
        boolean existe = usuarioService.checkIfExists(id);
        if (existe) {
            return ResponseEntity.ok("" + existe);
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("" + existe);
    }
}
