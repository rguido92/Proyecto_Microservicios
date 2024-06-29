package com.microservicios.comentarios.servicios;

import com.microservicios.comentarios.dto.*;
import com.microservicios.comentarios.entidades.Comentario;
import com.microservicios.comentarios.repositorios.IComentariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComentariosService {

    @Autowired
    private final IComentariosRepository iComentariosRepository;

    private RestTemplate restTemplate;

    public ComentariosService(IComentariosRepository iComentariosRepository) {
        this.iComentariosRepository = iComentariosRepository;
    }

    public String eliminarComentario(String id){
        Comentario existe = iComentariosRepository.findById(id).orElse(null);
        if(existe!= null){
            iComentariosRepository.deleteById(id);
            return "Se ha eliminado el comentario";
        }else return "La id del comentario no existe";
    }

    public String eliminarComentarios(){
        try {
            List<Comentario> listaComentarios =  iComentariosRepository.findAll();
            iComentariosRepository.deleteAll();
            return "Se han eliminado todos los comentarios";
        }catch (Exception e){return "No se han podido eliminar los comentarios";}
    }

    public CrearComentarioDTO crearComentario(CrearComentarioDTO comentarioDTO) {
        if (comentarioDTO == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo");
        }

        int idHotel = obtenerIdHotel(comentarioDTO.getNombreHotel());
        int idUsuario = obtenerIdUsuario(comentarioDTO.getNombreUsuario());
        int idReserva = comentarioDTO.getReservaId();

        UserPassDTO user = new UserPassDTO();
        user.setNombre(comentarioDTO.getNombreUsuario());
        user.setContrasena(comentarioDTO.getContrasena());

        if (validarUsuario(user)) {
            if (existeReserva(idReserva, idUsuario, idHotel) && !existeComentario(idReserva, idUsuario, idHotel)) {
                Comentario comentario = new Comentario();
                comentario.setComentario(comentarioDTO.getComentario());
                comentario.setHotelId(idHotel);
                comentario.setUsuarioId(idUsuario);
                comentario.setReservaId(idReserva);
                comentario.setPuntuacion(comentarioDTO.getPuntuacion());
                iComentariosRepository.save(comentario);
                return comentarioDTO;
            } else {
                throw new RuntimeException("No existe la reserva o el comentario ya existe");
            }
        } else {
            throw new RuntimeException("Usuario no válido");
        }
    }

    public List<ListarComentariosHotelDTO> listarComentarioHotel(ListarComentariosHotelDTO listarComentariosHotelDTO){
        int hotelid = obtenerIdHotel(listarComentariosHotelDTO.getNombreHotel());
        List<ListarComentariosHotelDTO> listaComentariosDTO = iComentariosRepository.findByHotelId(hotelid).stream()
                .map(ListarComentariosHotelDTO::new).toList();
        for (ListarComentariosHotelDTO comentariosHotelDTO : listaComentariosDTO) {
            comentariosHotelDTO.setNombreHotel(listarComentariosHotelDTO.getNombreHotel());
        }
        return  listaComentariosDTO;
    }

    public int obtenerIdUsuario(String nombre) {
        restTemplate = new RestTemplate();
        String url = "http://localhost:8702/usuarios/info/nombre/?nombre=" + nombre;
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, null, Integer.class);
            if (response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error: Respuesta no exitosa o cuerpo de respuesta nulo");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al realizar la solicitud al servicio de usuarios: " + e.getMessage());
        }
    }

    public boolean existeComentario(int reservaId, int usuarioId, int hotelId) {
        Comentario comentario = iComentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(hotelId, reservaId, usuarioId);
        if (comentario == null) return false;
        else {
            System.out.println(comentario.getComentario());
            return true;
        }
    }

    public boolean existeReserva(int reservaId, int usuarioId, int hotelId) {
        restTemplate = new RestTemplate();
        String url = String.format("http://localhost:8701/reservas/check?idUsuario=%d&idReserva=%d&idHotel=%d", usuarioId, reservaId, hotelId);
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error: Respuesta no exitosa");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al realizar la solicitud al servicio de reservas: " + e.getMessage());
        }
    }

    public boolean validarUsuario(UserPassDTO userPassDTO) {
        restTemplate = new RestTemplate();
        String url = "http://localhost:8702/usuarios/validar";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, userPassDTO, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }

    private int obtenerIdHotel(String nombreHotel) {
        restTemplate = new RestTemplate();
        String url = "http://localhost:8701/hotel/id?nombre=" + nombreHotel;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Integer.parseInt(response.getBody());
            } else {
                throw new RuntimeException("Error: Respuesta no exitosa");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al realizar la solicitud al servicio de hoteles: " + e.getMessage());
        }
    }

    public List<ListarComentariosHotelDTO> listarComentarioUsuario(UserPassDTO userPassDTO) {
        int idUsuario = obtenerIdUsuario(userPassDTO.getNombre());
        List<ListarComentariosHotelDTO> listaComentariosDto = iComentariosRepository.findByUsuarioId(idUsuario).stream()
                .map(ListarComentariosHotelDTO::new).toList();
        return  listaComentariosDto;
    }

    public List<ListarComentariosHotelDTO> mostrarComentarioReservaUsuario(MostrarComentarioReservaDTO mostrarComentarioReservaDTO) {
        int usuarioId= obtenerIdUsuario(mostrarComentarioReservaDTO.getNombre());
        List<ListarComentariosHotelDTO>listarComentariosUsuario = iComentariosRepository.findByUsuarioIdAndReservaId(usuarioId,mostrarComentarioReservaDTO.getReservaId()).stream().map(ListarComentariosHotelDTO::new).toList();
        return listarComentariosUsuario;
    }
    public Double puntuacionMediaHotel(int idHotel){
        try {
            return iComentariosRepository.puntuacionMediaHotel(idHotel).getUniqueMappedResult().getDouble("media");
        }catch (NullPointerException e){
            return -1.0;
        }

    }
    public String obtenerNombreApartirId(UserPassDTO usuarioContrasenhaDTO,Integer idHotel){
        RestTemplate restTemplate = new RestTemplate();
        String url =  "http://localhost:8701/reservas/hotel/nombre/{idHotel}";

        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("idHotel", idHotel);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, usuarioContrasenhaDTO, String.class, uriVariables);
            return responseEntity.getBody();
        }catch (Exception e){
            return null;
        }

    }
    public Integer obtenerIdApartirNombre(String nombreHotel, UserPassDTO userPassDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8701/hotel/id";

        // Crear un objeto MultiValueMap para enviar parámetros en el cuerpo de la solicitud POST
        MultiValueMap<String, String> uriVariables = new LinkedMultiValueMap<>();
        uriVariables.add("nombre", nombreHotel);

        try {
            // Enviar la solicitud POST con el cuerpo y los parámetros necesarios
            ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url, uriVariables, Integer.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            // Captura y maneja la excepción de HTTP
            return -1;
        }
    }

    public Double mediaPuntuacionPorUsuario(int idUsuario){
        try {
            return iComentariosRepository.mediaUsuario(idUsuario).getUniqueMappedResult().getDouble("media");
        }catch (NullPointerException e){
            return -1.0;
        }
    }
}
