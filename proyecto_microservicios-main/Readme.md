# Proyecto de Microservicios

## Descripción del Proyecto

Este proyecto consta de múltiples microservicios que gestionan diferentes aspectos de un sistema de reservas de hoteles. Los microservicios incluyen:

1. **Microservicio de Usuarios**: Gestión de usuarios del sistema.
2. **Microservicio de Reservas**: Gestión de hoteles, habitaciones y reservas.
3. **Microservicio de Comentarios**: Gestión de comentarios sobre las reservas.
4. **API Gateway**: Punto de entrada al sistema.
5. **Servidor Eureka**: Registro y descubrimiento de servicios.

---

## Microservicio de Usuarios

### Descripción

Se encarga de todas las gestiones relacionadas con los usuarios del sistema.

### Configuración Técnica

- **Nombre del servicio**: `usuarios`
- **API Rest**: Implementada para comunicación externa.
- **Ruta raíz**: `/usuarios`
- **Puerto**: `8702`
- **Hibernate**: Configurado en modo `validate`.
- **Base de datos**: MySQL, `usuariosProyecto`.

### Funcionalidades

1. **Crear un nuevo usuario (crearUsuario)**
    - **URL**: `/usuarios/registrar`
    - **Método**: `POST`
    - **Parámetros**: `{ nombre, correo_electronico, direccion, contraseña }`
    - **Respuesta**: Cadena indicando éxito o fallo.

2. **Actualizar usuario (actualizarUsuario)**
    - **URL**: `/usuarios/registrar`
    - **Método**: `PUT`
    - **Parámetros**: `{ id, nombre, correo_electronico, direccion, contraseña }`
    - **Respuesta**: Cadena indicando éxito o fallo.

3. **Eliminar usuario (eliminarUsuario)**
    - **URL**: `/usuarios`
    - **Método**: `DELETE`
    - **Parámetros**: `{ nombre, contraseña }`
    - **Respuesta**: Cadena indicando éxito o fallo.

4. **Validar usuario (validarUsuario)**
    - **URL**: `/usuarios/validar`
    - **Método**: `POST`
    - **Parámetros**: `{ nombre, contraseña }`
    - **Respuesta**: Booleano indicando validez.

5. **Obtener nombre de usuario por ID (obtenerInfoUsuarioPorId)**
    - **URL**: `/usuarios/info/id/{id}`
    - **Método**: `GET`
    - **Respuesta**: Cadena con el nombre del usuario.

6. **Obtener ID de usuario por nombre (obtenerInfoUsuarioPorNombre)**
    - **URL**: `/usuarios/info/nombre/{nombre}`
    - **Método**: `GET`
    - **Respuesta**: Cadena con el ID del usuario.

7. **Comprobar existencia de usuario (checkIfExist)**
    - **URL**: `/usuarios/checkIfExist/{id}`
    - **Método**: `GET`
    - **Respuesta**: Booleano indicando existencia.

---

## Microservicio de Reservas

### Descripción

Gestiona los datos de los hoteles, habitaciones y reservas hechas por los usuarios.

### Configuración Técnica

- **Nombre del servicio**: `reservas`
- **API Rest**: Implementada para comunicación externa.
- **Ruta raíz**: `/reservas`
- **Puerto**: `8701`
- **Hibernate**: Configurado en modo `validate`.
- **Base de datos**: MySQL, `reservasProyecto`.

### Funcionalidades

#### Gestión de Habitaciones

1. **Crear habitación (crearHabitación)**
    - **URL**: `/reservas/habitacion`
    - **Método**: `POST`
    - **Parámetros**: `{ numeroHabitacion, tipo, precio, idHotel }`
    - **Respuesta**: Cadena indicando éxito o fallo.

2. **Actualizar habitación (actualizarHabitacion)**
    - **URL**: `/reservas/habitacion`
    - **Método**: `PATCH`
    - **Parámetros**: `{ id, numeroHabitacion, tipo, precio, idHotel, disponible }`
    - **Respuesta**: Cadena indicando éxito o fallo.

3. **Eliminar habitación (eliminarHabitacion)**
    - **URL**: `/reservas/habitacion/{id}`
    - **Método**: `DELETE`
    - **Respuesta**: Cadena indicando éxito o fallo.

#### Gestión de Hoteles

1. **Crear hotel (crearHotel)**
    - **URL**: `/reservas/hotel`
    - **Método**: `POST`
    - **Parámetros**: `{ nombre, direccion }`
    - **Respuesta**: Cadena indicando éxito o fallo.

2. **Actualizar hotel (actualizarHotel)**
    - **URL**: `/reservas/hotel`
    - **Método**: `PATCH`
    - **Parámetros**: `{ id, nombre, direccion }`
    - **Respuesta**: Cadena indicando éxito o fallo.

3. **Eliminar hotel (eliminarHotel)**
    - **URL**: `/reservas/hotel/{id}`
    - **Método**: `DELETE`
    - **Respuesta**: Cadena indicando éxito o fallo.

4. **Obtener ID de hotel por nombre (obtenerIdApartirNombre)**
    - **URL**: `/reservas/hotel/id`
    - **Método**: `POST`
    - **Parámetros**: `{ nombre }`
    - **Respuesta**: Cadena con el ID del hotel.

5. **Obtener nombre de hotel por ID (obtenerNombreAPartirId)**
    - **URL**: `/reservas/hotel/nombre`
    - **Método**: `POST`
    - **Parámetros**: `{ id }`
    - **Respuesta**: Cadena con el nombre del hotel.

#### Gestión de Reservas

1. **Crear reserva (crearReserva)**
    - **URL**: `/reservas`
    - **Método**: `POST`
    - **Parámetros**: `{ fecha_inicio, fecha_fin, habitacion_id }`
    - **Respuesta**: Cadena indicando éxito o fallo.

2. **Cambiar estado de reserva (cambiarEstado)**
    - **URL**: `/reservas`
    - **Método**: `PATCH`
    - **Parámetros**: `{ reserva_id, estado }`
    - **Respuesta**: Cadena indicando éxito o fallo.

3. **Listar reservas del usuario (listarReservasUsuario)**
    - **URL**: `/reservas`
    - **Método**: `GET`
    - **Respuesta**: Lista con información de las reservas.

4. **Listar reservas según estado (listarReservasSegunEstado)**
    - **URL**: `/reservas`
    - **Método**: `GET`
    - **Parámetros**: `{ estado }`
    - **Respuesta**: Lista con información de las reservas.

5. **Comprobar reserva (checkReserva)**
    - **URL**: `/reservas/check`
    - **Método**: `GET`
    - **Parámetros**: `{ idUsuario, idHotel, idReserva }`
    - **Respuesta**: Booleano indicando existencia.

---

## Microservicio de Comentarios

### Descripción

Gestiona los comentarios que los usuarios hacen sobre las reservas.

### Configuración Técnica

- **Nombre del servicio**: `comentarios`
- **API GraphQL**: Implementada para comunicación externa.
- **Endpoint GraphIQL**: `/comentarios`
- **Puerto**: `8703`
- **Base de datos**: MongoDB, `comentariosProyecto`.
- **Colección**: `comentarios`

### Funcionalidades

1. **Crear comentario (crearComentario)**
    - **Parámetros**: `{ nombreHotel, idReserva, puntuacion, comentario }`
    - **Respuesta**: Objeto recibido a modo de confirmación.
    - **Validaciones**: Consultas a microservicios de reservas y usuarios, validación de existencia de reserva.

2. **Eliminar todos los comentarios (eliminarComentarios)**
    - **Respuesta**: Cadena indicando éxito o fallo.

3. **Eliminar comentario de usuario (eliminarComentarioDeUsuario)**
    - **Parámetros**: `{ idComentario }`
    - **Respuesta**: Cadena indicando éxito o fallo.

4. **Listar comentarios de hotel (listarComentariosHotel)**
    - **Parámetros**: `{ nombreHotel }`
    - **Respuesta**: Lista de comentarios.

5. **Listar comentarios de usuario (listarComentariosUsuario)**
    - **Respuesta**: Lista de comentarios.

6. **Mostrar comentario de usuario en reserva (mostrarComentarioUsuarioReserva)**
    - **Parámetros**: `{ idReserva }`
    - **Respuesta**: Lista de comentarios.

7. **Puntuación media de hotel (puntuacionMediaHotel)**
    - **Parámetros**: `{ nombreHotel }`
    - **Respuesta**: Double con la puntuación media.

8. **Puntuación media de usuario (puntuacionesMediasUsuario)**
    - **Respuesta**: Double con la puntuación media.

---

## API Gateway

### Descripción

Punto de entrada al sistema para acceder a los microservicios.

### Configuración Técnica

- **Nombre del servicio**: `gateway`
- **Puerto**: `8080`
- **Servidor Eureka**: No necesita estar registrado.

### Funcionalidades

- Acceso a las APIs Rest y GraphIQL de los microservicios.

---

## Servidor Eureka

### Descripción

Permite el registro y descubrimiento de servicios.

### Configuración Técnica

- **Puerto**: `8700`

### Funcionalidades

- Registro y descubrimiento de microservicios del sistema.

---

## Configuración y Despliegue

1. **Clonar el repositorio**:
    ```bash
    git clone <URL_REPOSITORIO>
    cd <NOMBRE_DEL_REPOSITORIO>
    ```

2. **Configurar bases de datos**:
    - Configurar las bases de datos MySQL y MongoDB según los archivos `usuariosProyecto.sql`, `reservasProyecto.sql` y `comentarios.json`.

3. **Construir y ejecutar los microservicios**:
    - Usar herramientas como Maven o Gradle para construir cada microservicio.
    - Ejecutar cada microservicio en su puerto correspondiente.

4. **Configurar Eureka y Gateway**:
    - Asegurarse de que Eureka esté corriendo antes de iniciar los microservicios.
    - Configurar el Gateway para enrutar correctamente las peticiones.

5. **Acceder a los servicios**:
    - Usar la URL del Gateway para acceder a las APIs Rest y GraphIQL.

---

## Autor

- Rodrigo Domingo Guido Arias

