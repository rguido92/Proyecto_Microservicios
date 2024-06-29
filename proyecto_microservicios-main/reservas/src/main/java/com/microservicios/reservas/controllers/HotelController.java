package com.microservicios.reservas.controllers;

import com.microservicios.reservas.dto.HotelDTO;
import com.microservicios.reservas.dto.UserpassDTO;
import com.microservicios.reservas.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservas/hotel")
public class HotelController {
    @Autowired
    private  HotelService hotelService;



    @PostMapping("")
    public ResponseEntity<String> crearHotel(@RequestBody HotelDTO hotelDTO) {
        if (hotelService.comprobarContrasena(hotelDTO.getUsuario(),hotelDTO.getContrasena())){
            String mensaje = hotelService.crearHotel(hotelDTO);
            return ResponseEntity.ok(mensaje);
        }else return ResponseEntity.ok().body("Usuario no valido");
    }

    @PatchMapping("")
    public ResponseEntity<String> actualizarHotel(@RequestBody HotelDTO hotelDTO) {
        if (hotelService.comprobarContrasena(hotelDTO.getUsuario(),hotelDTO.getContrasena())){
            String mensaje = hotelService.actualizarHotel(hotelDTO);
            return ResponseEntity.ok(mensaje);
        }else return ResponseEntity.ok().body("Usuario no valido");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHotel(@PathVariable int id, @RequestBody UserpassDTO userpassDTO) {
        if (hotelService.comprobarContrasena(userpassDTO.getNombre(),userpassDTO.getContraseña())){
            String mensaje = hotelService.eliminarHotel(id);
            return ResponseEntity.ok(mensaje);
        }else return ResponseEntity.ok().body("Usuario no valido");
    }

    @PostMapping("/id")
    public ResponseEntity<Integer> obtenerIdApartirNombre(@RequestParam String nombre, @RequestBody UserpassDTO userpassDTO) {
        if (hotelService.comprobarContrasena(userpassDTO.getNombre(),userpassDTO.getContraseña())){
            int id = hotelService.obtenerIdApartirNombre(nombre);
            return ResponseEntity.ok(id);
        }else return ResponseEntity.badRequest().body(-1);

    }

    @PostMapping("/nombre")
    public ResponseEntity<String> obtenerNombreAPartirId(@RequestParam int id, @RequestBody UserpassDTO userpassDTO) {
            if (hotelService.comprobarContrasena(userpassDTO.getNombre(),userpassDTO.getContraseña())){
                String nombre = hotelService.obtenerNombreAPartirId(id);
                return ResponseEntity.ok(nombre);
            }else return ResponseEntity.ok().body("Usuario no valido");

    }


}
