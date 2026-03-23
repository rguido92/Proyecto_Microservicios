# Checklist de URLs

## Servicios base

- Eureka: `http://localhost:8700`
- Gateway y panel web: `http://localhost:8080`
- Usuarios directo: `http://localhost:8702/usuarios`
- Reservas directo: `http://localhost:8701/reservas`
- Comentarios GraphQL directo: `http://localhost:8703/comentarios`

## Comprobaciones rápidas

1. Eureka carga y muestra instancias registradas.
2. El panel del gateway abre en `http://localhost:8080`.
3. Crear usuario desde el panel o con `POST /usuarios/registrar`.
4. Consultar usuarios con `GET /usuarios`.
5. Crear hotel con `POST /reservas/hotel`.
6. Listar hoteles con `GET /reservas/hotel`.
7. Crear habitación con `POST /reservas/habitacion`.
8. Listar habitaciones con `GET /reservas/habitacion`.
9. Crear reserva con `POST /reservas`.
10. Listar reservas por usuario con `POST /reservas/listar-usuario`.
11. Consultar GraphQL en comentarios con `POST /comentarios`.

## Endpoints útiles a través del gateway

- `GET http://localhost:8080/usuarios`
- `POST http://localhost:8080/usuarios/registrar`
- `PUT http://localhost:8080/usuarios/registrar`
- `DELETE http://localhost:8080/usuarios/`
- `GET http://localhost:8080/reservas/hotel`
- `POST http://localhost:8080/reservas/hotel`
- `GET http://localhost:8080/reservas/habitacion`
- `POST http://localhost:8080/reservas/habitacion`
- `POST http://localhost:8080/reservas`
- `PATCH http://localhost:8080/reservas`
- `POST http://localhost:8080/reservas/listar-usuario`
- `POST http://localhost:8080/reservas/listar-estado?estado=Pendiente`
- `GET http://localhost:8080/reservas/check?idUsuario=1&idReserva=1&idHotel=1`
- `POST http://localhost:8080/comentarios`

## MongoDB Atlas

- Copia `.env.atlas.example` a `.env` y sustituye `ATLAS_MONGODB_URI`.
- Arranca el stack Atlas con `docker compose -f docker-compose.atlas.yml up --build`.
- Si usas Atlas, no necesitas el contenedor local de MongoDB.
