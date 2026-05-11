# Datos de Prueba para el Proyecto de Microservicios

Este directorio contiene scripts para poblar las bases de datos con datos de prueba.

## Archivos

- `test-data-usuarios.sql` - Datos para MySQL (usuariosproyecto)
- `test-data-reservas.sql` - Datos para MySQL (reservasproyecto)
- `test-data-comentarios.js` - Datos para MongoDB (comentariosProyecto)

## Instrucciones de Uso

### 1. MySQL - Usuarios y Reservas

```bash
# Ejecutar script de usuarios
mysql -u root -pabc123 usuariosproyecto < test-data-usuarios.sql

# Ejecutar script de reservas
mysql -u root -pabc123 reservasproyecto < test-data-reservas.sql
```

O desde el cliente MySQL:
```sql
SOURCE test-data-usuarios.sql;
SOURCE test-data-reservas.sql;
```

### 2. MongoDB - Comentarios

```bash
# Abrir mongosh y ejecutar
use comentariosProyecto
```

Luego copiar y pegar el contenido de `test-data-comentarios.js` en la consola de mongosh.

## Datos Generados

### Usuarios (10 registros)
- 7 usuarios con rol USER
- 3 usuarios con rol ADMIN
- Nombres de máximo 10 caracteres (según restricción)

### Hoteles (5 registros)
- Hotel Paraíso (Cancún)
- Hotel Montana (Denver)
- Hotel Ciudad (Nueva York)
- Hotel Playa (Miami)
- Hotel Bosque (Oregón)

### Habitaciones (15 registros)
- 5 habitaciones por hotel
- Tipos: Individual, Doble, Suite
- Precios: $70-$280
- Algunas marcadas como no disponibles

### Reservas (15 registros)
- Referencian usuarios 1-10
- Estados: Confirmada, Pendiente, Cancelada
- Fechas: Mayo-Diciembre 2026

### Comentarios (12 registros)
- Puntuaciones: 1.0 - 5.0
- Referencian usuarios, hoteles y reservas existentes
- Fechas de creación variadas

## Validación

Una vez cargados los datos, puedes validar usando:

### API REST (Usuarios y Reservas)
```bash
# Obtener todos los usuarios
curl http://localhost:8080/api-usuarios/usuarios

# Obtener todas las reservas
curl http://localhost:8080/api-reservas/reservas

# Obtener todos los hoteles
curl http://localhost:8080/api-reservas/hoteles
```

### GraphQL (Comentarios)
Acceder a: http://localhost:8703/comentarios

```graphql
query {
  allComentarios {
    id
    usuarioId
    hotelId
    puntuacion
    comentario
    fechaCreacion
  }
}
```
