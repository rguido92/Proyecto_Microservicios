CREATE TABLE IF NOT EXISTS hotel (
    hotel_id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    PRIMARY KEY (hotel_id)
);

CREATE TABLE IF NOT EXISTS habitaciones (
    habitacion_id INT NOT NULL AUTO_INCREMENT,
    hotel_id INT NOT NULL,
    numero_habitacion INT NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    disponible BIT NOT NULL,
    PRIMARY KEY (habitacion_id),
    CONSTRAINT fk_habitacion_hotel
        FOREIGN KEY (hotel_id) REFERENCES hotel (hotel_id)
);

CREATE TABLE IF NOT EXISTS reserva (
    reserva_id INT NOT NULL AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    habitacion_id INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(255) NOT NULL,
    PRIMARY KEY (reserva_id),
    CONSTRAINT fk_reserva_habitacion
        FOREIGN KEY (habitacion_id) REFERENCES habitaciones (habitacion_id)
);
