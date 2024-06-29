Proyecto de Microservicios para Sistema de Reservas y Comentarios
Descripción del Proyecto

Este proyecto consiste en diseñar y desarrollar un sistema de microservicios para gestionar reservas de hoteles y comentarios de usuarios. El sistema está compuesto por los siguientes microservicios:

    Servicio de Reservas de Hoteles
    Servicio de Usuarios
    Servicio de Comentarios
    Servidor Eureka
    API Gateway

Arquitectura del Sistema
Servidor Eureka

El Servidor Eureka permite el registro y descubrimiento de los microservicios.

    Puerto: 8700

API Gateway

El API Gateway actúa como punto de entrada al sistema y enruta las solicitudes a los diferentes microservicios.

    Nombre del servicio: gateway
    Puerto: 8080
    Características:
        Utiliza el servidor Eureka para la comunicación entre servicios.
        No necesita registrarse en Eureka.
    Funcionalidades:
        Entrada y utilización de todas las funcionalidades del sistema.
        Acceso a todas las API Rest y GraphIQL implementadas por los servicios.

Microservicio de Usuarios

Gestiona todas las operaciones relacionadas con los usuarios del sistema.

    Nombre del servicio: usuarios
    Puerto: 8702
    Base de datos: MySQL (usuariosProyecto)
    Características:
        Implementa una API Rest.
        Raíz de la API: /usuarios
        Hibernate configurado en modo validate.
    Endpoints y Funcionalidades:
        Crear un nuevo usuario (/registrar, POST)
        Actualizar un usuario (/registrar, PUT)
        Eliminar usuario (/, DELETE)
        Validar usuario (/validar, POST)
        Obtener información de usuario por ID (/info/id/{id}, GET)
        Obtener información de usuario por nombre (/info/nombre/{nombre}, GET)
        Comprobar si un usuario existe (/checkIfExist/{id}, GET)

Microservicio de Reservas

Gestiona los datos de los hoteles, habitaciones y reservas.

    Nombre del servicio: reservas
    Puerto: 8701
    Base de datos: MySQL (reservasProyecto)
    Características:
        Implementa una API Rest.
        Raíz de la API: /reservas
        Hibernate configurado en modo validate.
    Endpoints y Funcionalidades:
        Gestión de habitaciones:
            Crear habitación (/reservas/habitacion, POST)
            Actualizar habitación (/reservas/habitacion, PATCH)
            Eliminar habitación (/reservas/habitacion, DELETE)
        Gestión de hoteles:
            Crear hotel (/reservas/hotel, POST)
            Actualizar hotel (/reservas/hotel, PATCH)
            Eliminar hotel (/reservas/hotel, DELETE)
            Obtener ID por nombre (/reservas/hotel/id, POST)
            Obtener nombre por ID (/reservas/hotel/nombre, POST)
        Gestión de reservas:
            Crear reserva (/reservas, POST)
            Cambiar estado de reserva (/reservas, PATCH)
            Listar reservas de usuario (/reservas, GET)
            Listar reservas por estado (/reservas, GET)
            Comprobar reserva (/reservas/check, GET)

Microservicio de Comentarios

Gestiona los comentarios de los usuarios sobre las reservas.

    Nombre del servicio: comentarios
    Puerto: 8703
    Base de datos: MongoDB (comentariosProyecto)
    Características:
        Implementa una API GraphQL.
        Endpoint de GraphIQL: /comentarios
        Colección: comentarios
    Endpoints y Funcionalidades:
        Crear comentario: Recibe (nombreHotel, idReserva, puntuación, comentario) y devuelve el objeto de confirmación.
        Eliminar todos los comentarios: Elimina todos los comentarios del sistema.
        Eliminar comentario de usuario: Elimina un comentario específico.
        Listar comentarios de un hotel: Lista comentarios de un hotel.
        Listar comentarios de un usuario: Lista comentarios de un usuario.
        Mostrar comentario de un usuario en una reserva: Muestra comentario específico.
        Puntuación media de un hotel: Calcula la puntuación media de un hotel.
        Puntuación media de un usuario: Calcula la puntuación media de un usuario.

Instrucciones de Configuración

    Instalar y configurar Eureka Server:
        Clonar el repositorio del servidor Eureka.
        Configurar el archivo application.yml con el puerto 8700.
        Ejecutar el servidor Eureka.

    Configurar API Gateway:
        Clonar el repositorio del gateway.
        Configurar el archivo application.yml con el puerto 8080 y la URL del servidor Eureka.
        Ejecutar el gateway.

    Configurar Microservicio de Usuarios:
        Clonar el repositorio del servicio de usuarios.
        Configurar application.yml con el puerto 8702 y la base de datos MySQL usuariosProyecto.
        Ejecutar el servicio de usuarios.

    Configurar Microservicio de Reservas:
        Clonar el repositorio del servicio de reservas.
        Configurar application.yml con el puerto 8701 y la base de datos MySQL reservasProyecto.
        Ejecutar el servicio de reservas.

    Configurar Microservicio de Comentarios:
        Clonar el repositorio del servicio de comentarios.
        Configurar application.yml con el puerto 8703 y la base de datos MongoDB comentariosProyecto.
        Ejecutar el servicio de comentarios.

Ejecución del Proyecto

    Iniciar el servidor Eureka.
    Iniciar el API Gateway.
    Iniciar el microservicio de usuarios.
    Iniciar el microservicio de reservas.
    Iniciar el microservicio de comentarios.

Asegúrese de que todos los microservicios estén registrados en el servidor Eureka y sean accesibles a través del API Gateway.
Contribuciones

Las contribuciones son bienvenidas. Por favor, abra un issue o envíe un pull request para sugerencias y mejoras.
Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulte el archivo LICENSE para obtener más detalles.
