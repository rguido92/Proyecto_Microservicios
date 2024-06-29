package com.microservicios.reservas.controllers;

import com.microservicios.reservas.dto.*;
import com.microservicios.reservas.services.HabitacionService;
import com.microservicios.reservas.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
//    @Autowired
//    private Usuario usuario;

    @Autowired
    private HabitacionService habitacionService;

    /**
     *
     * @param reservaDTO
     * @return
     */
    @PostMapping()
    public ResponseEntity<String> crearReserva(@Valid @RequestBody CrearReservaDTO reservaDTO) {
        String mensaje = reservaService.crearReserva(reservaDTO);
        return ResponseEntity.ok(mensaje);
    }

    /**
     *
     * @param cambioEstadoReservaDTO
     * @return
     */
    @PatchMapping()
    public ResponseEntity<String> cambiarEstadoReserva(@RequestBody ReservaCambiarEstadoDTO cambioEstadoReservaDTO) {
        String mensaje = reservaService.cambiarEstadoReserva(cambioEstadoReservaDTO);
        return ResponseEntity.ok(mensaje);
    }

    /**
     *
     * @param validarUsuarioDTO
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<ListarReservasDTO>> listarReservasUsuario(@RequestBody ValidarUsuarioDTO validarUsuarioDTO) {
        List<ListarReservasDTO> reservas = reservaService.listarReserva(validarUsuarioDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservas);
    }

    /**
     * http://localhost:8701/reservas/estado?estado=Pendiente
     * @param estado
     * @param validarUsuarioDTO
     * @return
     */
    @GetMapping("/{estado}")
    public ResponseEntity<List<ListarReservasDTO>> listarReservasEstado(@RequestParam String estado, @RequestBody ValidarUsuarioDTO validarUsuarioDTO) {
        if (reservaService.comprobarContrasena(validarUsuarioDTO.getNombre(), validarUsuarioDTO.getContrasena())) {
            List<ListarReservasDTO> reservas = reservaService.findbyEstado(estado);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservas);
        } else return null;
    }

    /**
     * http://localhost:8701/reservas/check?idUsuario=1&idReserva=1&idHotel=1
     * @param idUsuario
     * @param idReserva
     * @param idHotel
     * @return
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkReserva(@RequestParam int idUsuario,@RequestParam int idReserva,@RequestParam int idHotel) {
        CheckReservaDTO checkReservaDTO = new CheckReservaDTO();
        checkReservaDTO.setIdUsuario(idUsuario);
        checkReservaDTO.setIdReserva(idReserva);
        checkReservaDTO.setIdHotel(idHotel);
        boolean existeReserva = reservaService.checkReserva(checkReservaDTO);
        return ResponseEntity.ok(existeReserva);
    }
    // Para capturar los errores de validación
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
