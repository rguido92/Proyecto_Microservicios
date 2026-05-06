package com.microservicios.comentarios.servicios;

import com.microservicios.comentarios.dto.CrearComentarioDTO;
import com.microservicios.comentarios.dto.EliminarComentarioDTO;
import com.microservicios.comentarios.dto.ListarComentariosHotelDTO;
import com.microservicios.comentarios.dto.MostrarComentarioReservaDTO;
import com.microservicios.comentarios.dto.UserPassDTO;
import com.microservicios.comentarios.entidades.Comentario;
import com.microservicios.comentarios.repositorios.IComentariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComentariosService {
    @Value("${usuarios.service.url:http://localhost:8702}")
    private String usuariosServiceUrl;

    @Value("${reservas.service.url:http://localhost:8701}")
    private String reservasServiceUrl;

    @Autowired
    private final IComentariosRepository iComentariosRepository;

    private RestTemplate restTemplate;

    public ComentariosService(IComentariosRepository iComentariosRepository) {
        this.iComentariosRepository = iComentariosRepository;
    }

    public String eliminarComentario(String id) {
        Comentario existe = iComentariosRepository.findById(id).orElse(null);
        if (existe != null) {
            iComentariosRepository.deleteById(id);
            return "Se ha eliminado el comentario";
        }
        return "La id del comentario no existe";
    }

    public String eliminarComentarios() {
        try {
            iComentariosRepository.deleteAll();
            return "Se han eliminado todos los comentarios";
        } catch (Exception e) {
            return "No se han podido eliminar los comentarios";
        }
    }

    public CrearComentarioDTO crearComentario(CrearComentarioDTO comentarioDTO) {
        if (comentarioDTO == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo");
        }

        UserPassDTO user = new UserPassDTO();
        user.setNombre(comentarioDTO.getNombreUsuario());
        user.setContrasena(comentarioDTO.getContrasena());

        int idHotel = obtenerIdHotel(comentarioDTO.getNombreHotel(), user);
        int idUsuario = obtenerIdUsuario(comentarioDTO.getNombreUsuario());
        int idReserva = comentarioDTO.getReservaId();

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
            }
            throw new RuntimeException("No existe la reserva o el comentario ya existe");
        }
        throw new RuntimeException("Usuario no valido");
    }

    public List<ListarComentariosHotelDTO> listarComentarioHotel(ListarComentariosHotelDTO listarComentariosHotelDTO) {
        UserPassDTO user = new UserPassDTO(listarComentariosHotelDTO.getNombre(), listarComentariosHotelDTO.getContrasena());
        int hotelId = obtenerIdHotel(listarComentariosHotelDTO.getNombreHotel(), user);
        List<ListarComentariosHotelDTO> listaComentariosDTO = iComentariosRepository.findByHotelId(hotelId).stream()
                .map(ListarComentariosHotelDTO::new)
                .toList();
        for (ListarComentariosHotelDTO comentarioDTO : listaComentariosDTO) {
            comentarioDTO.setNombreHotel(listarComentariosHotelDTO.getNombreHotel());
        }
        return listaComentariosDTO;
    }

    public int obtenerIdUsuario(String nombre) {
        restTemplate = new RestTemplate();
        String url = usuariosServiceUrl + "/usuarios/info/nombre/?nombre=" + nombre;
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, null, Integer.class);
            if (response.getBody() != null) {
                return response.getBody();
            }
            throw new RuntimeException("Respuesta vacia del servicio de usuarios");
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar usuarios: " + e.getMessage());
        }
    }

    public boolean existeComentario(int reservaId, int usuarioId, int hotelId) {
        Comentario comentario = iComentariosRepository.findByHotelIdAndReservaIdAndUsuarioId(hotelId, reservaId, usuarioId);
        return comentario != null;
    }

    public boolean existeReserva(int reservaId, int usuarioId, int hotelId) {
        restTemplate = new RestTemplate();
        String url = String.format("%s/reservas/check?idUsuario=%d&idReserva=%d&idHotel=%d", reservasServiceUrl, usuarioId, reservaId, hotelId);
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Boolean.TRUE.equals(response.getBody());
            }
            throw new RuntimeException("Respuesta no valida del servicio de reservas");
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar reservas: " + e.getMessage());
        }
    }

    public boolean validarUsuario(UserPassDTO userPassDTO) {
        restTemplate = new RestTemplate();
        String url = usuariosServiceUrl + "/usuarios/validar";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, userPassDTO, Boolean.class);
        return Boolean.TRUE.equals(response.getBody());
    }

    private int obtenerIdHotel(String nombreHotel, UserPassDTO userPassDTO) {
        restTemplate = new RestTemplate();
        String url = reservasServiceUrl + "/reservas/hotel/id?nombre=" + nombreHotel;
        try {
            ResponseEntity<Integer> response = restTemplate.postForEntity(url, userPassDTO, Integer.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
            throw new RuntimeException("Respuesta no valida del servicio de hoteles");
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar hoteles: " + e.getMessage());
        }
    }

    public List<ListarComentariosHotelDTO> listarComentarioUsuario(UserPassDTO userPassDTO) {
        int idUsuario = obtenerIdUsuario(userPassDTO.getNombre());
        return iComentariosRepository.findByUsuarioId(idUsuario).stream()
                .map(ListarComentariosHotelDTO::new)
                .toList();
    }

    public List<ListarComentariosHotelDTO> mostrarComentarioReservaUsuario(MostrarComentarioReservaDTO mostrarComentarioReservaDTO) {
        int usuarioId = obtenerIdUsuario(mostrarComentarioReservaDTO.getNombre());
        return iComentariosRepository.findByUsuarioIdAndReservaId(usuarioId, mostrarComentarioReservaDTO.getReservaId()).stream()
                .map(ListarComentariosHotelDTO::new)
                .toList();
    }

    public Double puntuacionMediaHotel(int idHotel) {
        try {
            return iComentariosRepository.puntuacionMediaHotel(idHotel).getUniqueMappedResult().getDouble("media");
        } catch (NullPointerException e) {
            return -1.0;
        }
    }

    public String obtenerNombreApartirId(UserPassDTO usuarioContrasenhaDTO, Integer idHotel) {
        RestTemplate restTemplate = new RestTemplate();
        String url = reservasServiceUrl + "/reservas/hotel/nombre?id=" + idHotel;
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, usuarioContrasenhaDTO, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer obtenerIdApartirNombre(String nombreHotel, UserPassDTO userPassDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String url = reservasServiceUrl + "/reservas/hotel/id?nombre=" + nombreHotel;
        try {
            ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url, userPassDTO, Integer.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            return -1;
        }
    }

    public Double mediaPuntuacionPorUsuario(int idUsuario) {
        try {
            return iComentariosRepository.mediaUsuario(idUsuario).getUniqueMappedResult().getDouble("media");
        } catch (NullPointerException e) {
            return -1.0;
        }
    }
}
