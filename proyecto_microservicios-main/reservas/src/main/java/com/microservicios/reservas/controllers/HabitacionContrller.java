package com.microservicios.reservas.controllers;

import com.microservicios.reservas.dto.CrearHabitacionDTO;
import com.microservicios.reservas.dto.HabitacionDTO;
import com.microservicios.reservas.dto.UserpassDTO;
import com.microservicios.reservas.models.Habitacion;
import com.microservicios.reservas.services.HabitacionService;
import com.microservicios.reservas.services.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reservas/habitacion")
public class HabitacionContrller {
    @Autowired
    private HabitacionService habitacionService;


    @PostMapping("")
    public ResponseEntity<String> crearHabitacion(@Valid  @RequestBody CrearHabitacionDTO habitacionDTO) {
        String mensaje = habitacionService.crearHabitacion(habitacionDTO);
        return ResponseEntity.ok(mensaje);
    }

    @PatchMapping("")
    public ResponseEntity<String> actualizarHabitacion(@Valid @RequestBody CrearHabitacionDTO habitacionDTO) {
        String mensaje = habitacionService.actualizarHabitacion(habitacionDTO);
        return ResponseEntity.ok(mensaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHabitacion(@PathVariable int id,@RequestBody UserpassDTO userpass) {
        if (habitacionService.comprobarContrasena(userpass.getNombre(), userpass.getContraseña())){

            String mensaje = habitacionService.eliminarHabitacion(id);

            return ResponseEntity.ok(mensaje);
        }else  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
    }
}
