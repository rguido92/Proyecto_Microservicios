CREATE DATABASE IF NOT EXISTS usuariosproyecto;
CREATE DATABASE IF NOT EXISTS reservasproyecto;

USE usuariosproyecto;

CREATE TABLE IF NOT EXISTS usuario (
    usuario_id INT NOT NULL AUTO_INCREMENT,
    contrasena VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(80) NOT NULL,
    direccion VARCHAR(80) NOT NULL,
    nombre VARCHAR(10) NOT NULL,
    fecha_registro DATE,
    rol VARCHAR(20) NOT NULL DEFAULT 'USER',
    telefono VARCHAR(20),
    PRIMARY KEY (usuario_id)
);

INSERT INTO usuario (contrasena, correo_electronico, direccion, nombre, fecha_registro, rol, telefono)
SELECT 'admin123', 'admin@micro.local', 'Panel central', 'admin', CURDATE(), 'ADMIN', '600000000'
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE nombre = 'admin'
);

USE reservasproyecto;

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
    estado VARCHAR(255) NOT NULL,
    fecha_fin DATE NOT NULL,
    fecha_inicio DATE NOT NULL,
    usuario_id INT NOT NULL,
    habitacion_id INT NOT NULL,
    PRIMARY KEY (reserva_id),
    CONSTRAINT fk_reserva_habitacion
        FOREIGN KEY (habitacion_id) REFERENCES habitaciones (habitacion_id)
);
