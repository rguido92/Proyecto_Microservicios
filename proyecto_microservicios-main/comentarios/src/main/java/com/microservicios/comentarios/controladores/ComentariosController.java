package com.microservicios.comentarios.controladores;

import com.microservicios.comentarios.dto.*;
import com.microservicios.comentarios.servicios.ComentariosService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Controller
public class ComentariosController {
    @Autowired
    private  ComentariosService comentariosService;


    @MutationMapping
    public CrearComentarioDTO crearComentario(@Argument CrearComentarioDTO comentarioDTO) {
        if (comentarioDTO == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo");
        }
        try {
            return comentariosService.crearComentario(comentarioDTO);
        } catch (Exception e) {
            // Log and handle exception
            e.printStackTrace();
            throw new RuntimeException("Error al crear comentario: " + e.getMessage());
        }
    }
    @MutationMapping
    public String eliminarComentarios(){
        return comentariosService.eliminarComentarios();
    }


    @MutationMapping
    public String eliminarComentarioDTO(@Argument EliminarComentarioDTO eliminarComentarioDTO) {
        if (comentariosService.validarUsuario(new UserPassDTO(eliminarComentarioDTO.getNombre(), eliminarComentarioDTO.getContrasena()))) {
            return comentariosService.eliminarComentario(eliminarComentarioDTO.getId());
        } else {
            return "Error al autenticarse";
        }
    }

    @QueryMapping
    public List<ListarComentariosHotelDTO>listarComentariosHotel(@Argument ListarComentariosHotelDTO listarComentariosHotelDTO){
        UserPassDTO userPassDTO = new UserPassDTO(listarComentariosHotelDTO.getNombre(),listarComentariosHotelDTO.getContrasena());
        if (comentariosService.validarUsuario(userPassDTO)){
            return comentariosService.listarComentarioHotel(listarComentariosHotelDTO);
        }else return Collections.emptyList();

    }

    @QueryMapping
    public List<ListarComentariosHotelDTO>listarComentariosUsuario(@Argument UserPassDTO userPassDTO){
        if (comentariosService.validarUsuario(userPassDTO)){
           return comentariosService.listarComentarioUsuario(userPassDTO);
        }return  Collections.emptyList();
    }
    @QueryMapping
    public Double puntuacionMediaHotel(@Argument ObtenerHotelDTO obtenerHotelDTO){
        UserPassDTO user = new UserPassDTO();
        user.setNombre(obtenerHotelDTO.getNombre());
        user.setContrasena(obtenerHotelDTO.getContrasena());
       if (comentariosService.validarUsuario(user)){
           int idHotel = comentariosService.obtenerIdApartirNombre(obtenerHotelDTO.getNombreHotel(),user);
           System.out.println("HOTEL ID "+idHotel);
           return comentariosService.puntuacionMediaHotel(idHotel);
       }else return -1.0;
    }

    @QueryMapping
    public List<ListarComentariosHotelDTO>mostrarComentarioUsuarioReserva(@Argument MostrarComentarioReservaDTO mostrarComentarioReservaDTO){
        UserPassDTO userPassDTO= new UserPassDTO();
        userPassDTO.setNombre(mostrarComentarioReservaDTO.getNombre());
        userPassDTO.setContrasena(mostrarComentarioReservaDTO.getContrasena());
        if (comentariosService.validarUsuario(userPassDTO)){
            System.out.printf("pasa");
            return  comentariosService.mostrarComentarioReservaUsuario(mostrarComentarioReservaDTO);
        }else return  Collections.emptyList();
    }
    @QueryMapping
    public Double puntuacionesMediasUsuario(@Argument UserPassDTO userPassDTO){
        if(comentariosService.validarUsuario(userPassDTO)){
            int idUsuario = comentariosService.obtenerIdUsuario(userPassDTO.getNombre());
            return comentariosService.mediaPuntuacionPorUsuario(idUsuario);
        }else return null;
    }
}
