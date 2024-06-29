package com.microservicios.usuarios.repositorio;

import com.microservicios.usuarios.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<Usuario, Integer> {
    // @Query("SELECT u FROM Usuario u WHERE u.nombre = :nombre")
    Usuario findByNombre(String nombre);
    List<Usuario> findAllByNombre(String nombre);
    Usuario findByNombreAndContrasena(String nombre, String contrasena);
}
