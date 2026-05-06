package com.microservicios.comentarios.entidades;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comentarios")
public class Comentario {
    @Id
    private String id;
    private int usuarioId;
    private int hotelId;
    private int reservaId;
    private double  puntuacion;
    private String comentario;
    private LocalDateTime fechaCreacion;

}
