-- Datos de prueba para el microservicio de reservas
-- Base de datos: reservasproyecto
-- Tablas: hotel, habitaciones, reserva

USE reservasproyecto;

-- Insertar hoteles de prueba
INSERT INTO hotel (nombre, direccion) VALUES
('Hotel Paraíso', 'Playa Blanca 123, Cancún'),
('Hotel Montana', 'Cerro Alto 456, Denver'),
('Hotel Ciudad', 'Centro 789, Nueva York'),
('Hotel Playa', 'Costa Azul 321, Miami'),
('Hotel Bosque', 'Selva Verde 654, Oregón');

-- Insertar habitaciones de prueba (asociadas a los hoteles)
INSERT INTO habitaciones (hotel_id, numero_habitacion, tipo, precio, disponible) VALUES
(1, 101, 'Individual', 80.00, true),
(1, 102, 'Doble', 120.00, true),
(1, 103, 'Suite', 250.00, false),
(2, 201, 'Individual', 70.00, true),
(2, 202, 'Doble', 110.00, true),
(2, 203, 'Suite', 220.00, true),
(3, 301, 'Individual', 90.00, true),
(3, 302, 'Doble', 130.00, false),
(3, 303, 'Suite', 280.00, true),
(4, 401, 'Individual', 85.00, true),
(4, 402, 'Doble', 125.00, true),
(4, 403, 'Suite', 260.00, true),
(5, 501, 'Individual', 75.00, true),
(5, 502, 'Doble', 115.00, true),
(5, 503, 'Suite', 240.00, false);

-- Insertar reservas de prueba (usuario_id hace referencia a usuarios del otro microservicio)
INSERT INTO reserva (estado, fecha_fin, fecha_inicio, usuario_id, habitacion_id) VALUES
('Confirmada', '2026-05-15', '2026-05-10', 1, 1),
('Pendiente', '2026-05-18', '2026-05-12', 2, 2),
('Confirmada', '2026-06-05', '2026-06-01', 3, 4),
('Cancelada', '2026-05-25', '2026-05-20', 4, 5),
('Confirmada', '2026-07-15', '2026-07-10', 5, 7),
('Pendiente', '2026-06-20', '2026-06-15', 6, 8),
('Confirmada', '2026-08-05', '2026-08-01', 7, 10),
('Pendiente', '2026-07-25', '2026-07-20', 8, 11),
('Confirmada', '2026-09-15', '2026-09-10', 9, 13),
('Cancelada', '2026-08-20', '2026-08-15', 10, 14),
('Pendiente', '2026-10-05', '2026-10-01', 1, 3),
('Confirmada', '2026-09-25', '2026-09-20', 2, 6),
('Pendiente', '2026-11-15', '2026-11-10', 3, 9),
('Confirmada', '2026-10-25', '2026-10-20', 4, 12),
('Pendiente', '2026-12-05', '2026-12-01', 5, 15);

-- Verificar datos insertados
SELECT * FROM hotel;
SELECT * FROM habitaciones;
SELECT * FROM reserva;
