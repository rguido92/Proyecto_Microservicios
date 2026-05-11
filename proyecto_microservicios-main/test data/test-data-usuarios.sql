-- Datos de prueba para el microservicio de usuarios
-- Base de datos: usuariosproyecto
-- Tabla: usuario

USE usuariosproyecto;

-- Insertar usuarios de prueba
INSERT INTO usuario (contrasena, correo_electronico, direccion, nombre, fecha_registro, rol, telefono) VALUES
('pass123', 'juan.perez@email.com', 'Calle Mayor 123, Madrid', 'Juan', '2026-01-01', 'USER', '600000001'),
('pass456', 'maria.garcia@email.com', 'Avenida Sol 45, Barcelona', 'Maria', '2026-01-01', 'USER', '600000002'),
('pass789', 'carlos.lopez@email.com', 'Plaza Luna 78, Valencia', 'Carlos', '2026-01-01', 'ADMIN', '600000003'),
('pass321', 'ana.martinez@email.com', 'Calle Estrella 12, Sevilla', 'Ana', '2026-01-01', 'USER', '600000004'),
('pass654', 'luis.rodriguez@email.com', 'Avenida Mar 89, Bilbao', 'Luis', '2026-01-01', 'USER', '600000005'),
('pass987', 'elena.fernandez@email.com', 'Calle Rio 34, Zaragoza', 'Elena', '2026-01-01', 'ADMIN', '600000006'),
('pass147', 'pablo.sanchez@email.com', 'Plaza Monte 56, Malaga', 'Pablo', '2026-01-01', 'USER', '600000007'),
('pass258', 'sofia.ramirez@email.com', 'Calle Bosque 90, Murcia', 'Sofia', '2026-01-01', 'USER', '600000008'),
('pass369', 'diego.torres@email.com', 'Avenida Lago 23, Palma', 'Diego', '2026-01-01', 'USER', '600000009'),
('pass741', 'lucia.navarro@email.com', 'Calle Prado 67, Toledo', 'Lucia', '2026-01-01', 'ADMIN', '600000010');

-- Verificar datos insertados
SELECT * FROM usuario;
