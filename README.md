# CineX — Sistema de gestión de cine

Aplicación web para administrar un cine: películas, salas, funciones, reservas, productos de confitería y usuarios. Incluye una API REST en Java con Spring Boot, una base de datos MySQL y un frontend web con panel de administración.

> Proyecto final académico — Universidad Cooperativa de Colombia, noviembre de 2025.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black)

---

## Funcionalidades

- **Cartelera:** catálogo de películas con título, duración y descripción.
- **Salas y funciones:** gestión de salas de cine y horarios de cada película.
- **Reservas:** registro de reservas y bookings de los usuarios.
- **Confitería:** gestión de productos y precios.
- **Usuarios:** registro y administración de usuarios.
- **Panel de administración:** interfaz web para crear, editar y eliminar registros consumiendo la API.

## Arquitectura

El backend sigue una arquitectura en capas:

```
Controller  →  Service (interfaz + implementación)  →  Repository (Spring Data JPA)  →  MySQL
```

```
CineX---Proyecto-Final/
├── CineX-Backend/
│   └── cineX_spring_boot/demo/src/main/java/com/example/demo/
│       ├── controllers/    # Endpoints REST
│       ├── services/       # Lógica de negocio
│       ├── repositories/   # Acceso a datos con JPA
│       └── entities/       # Modelos: Movie, CinemaRoom, Showtime, Booking, Reserva, Producto, Usuario
├── CineX-Database/
│   └── BD - CineX.sql      # Script de creación de la base de datos
└── CineX-Frontend/
    ├── html/               # index, movie, about, admin
    ├── css/
    └── js/app.js           # Consumo de la API con fetch
```

## API REST

Cada recurso expone operaciones CRUD (`GET`, `POST`, `PUT`, `DELETE`):

| Recurso | Ruta base |
| :-- | :-- |
| Películas | `/movies` |
| Salas | `/rooms` |
| Funciones | `/showtimes` |
| Bookings | `/bookings` |
| Reservas | `/reservas` |
| Productos | `/productos` |
| Usuarios | `/usuarios` |

Ejemplo:

```http
GET /movies/1
```

## Tecnologías

| Capa | Tecnologías |
| :-- | :-- |
| Backend | Java 21, Spring Boot 3.5.6, Spring Web, Spring Data JPA, Maven |
| Base de datos | MySQL |
| Frontend | HTML, CSS, JavaScript (Fetch API) |

## Cómo ejecutarlo

### Requisitos

- Java 21
- MySQL 8

### Pasos

1. Clona el repositorio:
   ```bash
   git clone https://github.com/AlexanderM-Code/CineX---Proyecto-Final.git
   cd CineX---Proyecto-Final
   ```
2. Crea la base de datos ejecutando `CineX-Database/BD - CineX.sql` en MySQL.
3. Revisa el usuario y la contraseña de MySQL en `CineX-Backend/cineX_spring_boot/demo/src/main/resources/application.properties`.
4. Inicia el backend:
   ```bash
   cd CineX-Backend/cineX_spring_boot/demo
   ./mvnw spring-boot:run
   ```
   La API queda disponible en `http://localhost:8080`.
5. Abre `CineX-Frontend/html/index.html` en el navegador.

## Autor

**Alexander Mejía Ospina** — Estudiante de Ingeniería de Software

[GitHub](https://github.com/AlexanderM-Code) · [LinkedIn](https://www.linkedin.com/in/alexander-mejia-ospina)
