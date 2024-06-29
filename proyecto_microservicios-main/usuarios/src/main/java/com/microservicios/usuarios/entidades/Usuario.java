package com.microservicios.usuarios.entidades;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id") // Agrega esta anotación para que coincida con el nombre de la columna en la base de datos
    private int usuario_id;

    public static String prueba;
    @NonNull
    @Column(length = 10,name = "nombre")
    private String nombre;

    @NonNull
    @Column(length = 80)
    private String correo_electronico;

    @NonNull
    @Column(length = 80)
    private String direccion;

    @NonNull
    @Column(length = 100)
    private String contrasena;


}
