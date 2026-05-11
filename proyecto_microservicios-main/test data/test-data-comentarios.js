// Datos de prueba para el microservicio de comentarios (MongoDB)
// Base de datos: comentariosProyecto
// Colección: comentarios
//
// Ejecutar en mongosh o MongoDB Compass:
// 1. Abrir mongosh
// 2. use comentariosProyecto
// 3. Copiar y pegar el siguiente código

db.comentarios.insertMany([
  {
    usuarioId: 1,
    hotelId: 1,
    reservaId: 1,
    puntuacion: 4.5,
    comentario: "Excelente hotel, muy buena atención y ubicación cerca de la playa",
    fechaCreacion: new Date("2026-05-16T10:30:00")
  },
  {
    usuarioId: 2,
    hotelId: 1,
    reservaId: 2,
    puntuacion: 5.0,
    comentario: "Habitación doble muy cómoda, desayuno buffet excelente",
    fechaCreacion: new Date("2026-05-19T14:20:00")
  },
  {
    usuarioId: 3,
    hotelId: 2,
    reservaId: 3,
    puntuacion: 3.5,
    comentario: "Hotel en la montaña, buena vista pero el servicio es lento",
    fechaCreacion: new Date("2026-06-06T09:15:00")
  },
  {
    usuarioId: 5,
    hotelId: 3,
    reservaId: 5,
    puntuacion: 4.0,
    comentario: "Bien ubicado en el centro, habitación individual pequeña pero funcional",
    fechaCreacion: new Date("2026-07-16T16:45:00")
  },
  {
    usuarioId: 7,
    hotelId: 4,
    reservaId: 7,
    puntuacion: 4.8,
    comentario: "Increíble vista al mar, personal muy amable y atento",
    fechaCreacion: new Date("2026-08-06T11:30:00")
  },
  {
    usuarioId: 8,
    hotelId: 4,
    reservaId: 8,
    puntuacion: 3.0,
    comentario: "La habitación necesita mantenimiento, aire acondicionado ruidoso",
    fechaCreacion: new Date("2026-07-26T13:10:00")
  },
  {
    usuarioId: 9,
    hotelId: 5,
    reservaId: 9,
    puntuacion: 5.0,
    comentario: "Una experiencia única en el bosque, muy recomendable para desconectar",
    fechaCreacion: new Date("2026-09-16T10:00:00")
  },
  {
    usuarioId: 1,
    hotelId: 1,
    reservaId: 11,
    puntuacion: 4.2,
    comentario: "Segunda vez en este hotel, sigue manteniendo la calidad",
    fechaCreacion: new Date("2026-10-06T15:30:00")
  },
  {
    usuarioId: 2,
    hotelId: 2,
    reservaId: 12,
    puntuacion: 4.5,
    comentario: "La suite es espectacular, vale totalmente la pena",
    fechaCreacion: new Date("2026-09-26T12:45:00")
  },
  {
    usuarioId: 3,
    hotelId: 3,
    reservaId: 13,
    puntuacion: 2.5,
    comentario: "No cumplieron con lo prometido, habitación sucia al llegar",
    fechaCreacion: new Date("2026-11-16T09:20:00")
  },
  {
    usuarioId: 5,
    hotelId: 4,
    reservaId: 15,
    puntuacion: 3.8,
    comentario: "Buena relación calidad-precio, pero la piscina estaba en mantenimiento",
    fechaCreacion: new Date("2026-12-06T14:15:00")
  },
  {
    usuarioId: 10,
    hotelId: 3,
    reservaId: 14,
    puntuacion: 1.0,
    comentario: "Pésima experiencia, cancelaron mi reserva sin avisar",
    fechaCreacion: new Date("2026-10-26T10:10:00")
  }
]);

// Verificar datos insertados
print("Comentarios insertados:");
db.comentarios.find().sort({ fechaCreacion: -1 }).forEach(printjson);

// Estadísticas
print("\nTotal de comentarios:", db.comentarios.countDocuments());
print("Puntuación promedio:", db.comentarios.aggregate([
  { $group: { _id: null, avg: { $avg: "$puntuacion" } } }
]).toArray()[0].avg);
