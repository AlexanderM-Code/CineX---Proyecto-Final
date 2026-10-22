# CineX — Sistema de gestión de cine

[![Backend](https://github.com/AlexanderM-Code/CineX---Proyecto-Final/actions/workflows/backend.yml/badge.svg)](https://github.com/AlexanderM-Code/CineX---Proyecto-Final/actions/workflows/backend.yml)

Aplicación web para administrar un cine: cartelera, salas, funciones, venta de sillas, productos de confitería, usuarios y reservas. Tiene una **API REST en Java con Spring Boot**, una base de datos **MySQL** y un frontend web que muestra películas reales desde la API de **TMDB** e incluye un panel de administración.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=postman&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black)

---

## Historia del proyecto

| Versión | Fecha | Qué incluye |
| :-- | :-- | :-- |
| **1.0** | Nov. 2025 | Proyecto final académico en equipo: estructura en capas, frontend con TMDB y CRUD de usuarios, productos y reservas en MySQL. |
| **2.0** | Oct. 2026 | Película, sala, función y compra de sillas pasan de datos en memoria a MySQL; reglas de negocio, validaciones, manejo de errores, Swagger, 17 pruebas, Docker Compose, colección de Postman y CI. |

## Reglas de negocio

- **No se venden más sillas que la capacidad de la sala.** Si quedan 2 sillas y se piden 3, la API responde `409` con *"Solo quedan 2 sillas disponibles para esta función"*.
- **Una sala no puede tener dos funciones a la misma hora.** Se valida en el servicio y con una restricción única en MySQL.
- **No se borra lo que está en uso:** una película o sala con funciones, o una función con compras, responde `409`.
- **Los nombres de sala no se repiten**, sin importar mayúsculas.

## Arquitectura

```
Frontend (HTML, CSS, JS) ──► API de TMDB (películas reales)
        │
        ▼
Controller  →  Service (reglas de negocio)  →  Repository (Spring Data JPA)  →  MySQL
        │
        └─ GlobalExceptionHandler: errores en JSON con código 400, 404 o 409
```

```
CineX-Backend/cineX_spring_boot/demo/src/main/java/com/example/demo/
├── controllers/   Endpoints REST documentados con OpenAPI
├── services/      Interfaces e implementaciones con las reglas de negocio
├── repositories/  Spring Data JPA (incluye una consulta JPQL de sillas vendidas)
├── entities/      Movie, CinemaRoom, Showtime, Booking, Usuario, Producto, Reserva
├── exceptions/    Errores de negocio y manejador global
└── config/        CORS, Swagger y datos de ejemplo
CineX-Database/    Script del esquema MySQL
CineX-Frontend/    Sitio web y panel de administración
postman/           Colección con 26 solicitudes y pruebas automáticas
```

## API

Documentación interactiva en **`/swagger-ui.html`** al ejecutar el backend.

| Recurso | Ruta | Operaciones |
| :-- | :-- | :-- |
| Películas | `/movies` | CRUD |
| Salas | `/rooms` | CRUD |
| Funciones | `/showtimes` (`?movieId=`) | CRUD + `GET /showtimes/{id}/availability` |
| Compra de sillas | `/bookings` (`?showtimeId=`) | CRUD |
| Usuarios | `/usuarios` | CRUD |
| Productos | `/productos` | CRUD |
| Reservas de confitería | `/reservas` | CRUD |

Ejemplo de error:

```json
{
  "estado": 409,
  "error": "Conflict",
  "mensaje": "Solo quedan 2 sillas disponibles para esta función",
  "ruta": "/bookings"
}
```

## Ejecutarlo

### Con Docker (todo con un comando)

```bash
cp .env.example .env
docker compose up -d --build
```

- Frontend: http://localhost:5500
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

### Sin Docker

Requisitos: Java 21 y MySQL 8.

```bash
mysql -u root -p < "CineX-Database/BD - CineX.sql"
cd CineX-Backend/cineX_spring_boot/demo
DB_USER=root DB_PASSWORD=tu_clave ./mvnw spring-boot:run
```

Abre `CineX-Frontend/index.html` en el navegador.

## Pruebas

- **17 pruebas con JUnit 5:** pruebas de integración con MockMvc sobre una base H2 en memoria (CRUD, validaciones, errores y reglas de negocio) y una prueba unitaria con Mockito de la regla de capacidad.
- **Colección de Postman** (`postman/`): 26 solicitudes con pruebas automáticas que recorren toda la API.
- **GitHub Actions** ejecuta en cada push las pruebas de JUnit y luego arranca la API real con MySQL y corre la colección de Postman con Newman.

```bash
cd CineX-Backend/cineX_spring_boot/demo && ./mvnw test
npx newman run postman/CineX.postman_collection.json
```

## Equipo

Proyecto desarrollado en equipo para la Universidad Cooperativa de Colombia (versión 1.0):

| Integrante | Aporte en la versión 1.0 |
| :-- | :-- |
| [Alexander Mejía Ospina](https://github.com/AlexanderM-Code) | Estructura inicial del repositorio, integración y revisión de los pull requests |
| [Santiago Mayorga](https://github.com/qxsantx) | Capas de entidades y servicios |
| [Santiago Rodríguez](https://github.com/santiago12R) | Controladores y base de datos |
| [Ronald](https://github.com/RONALD-GM) | Estilos y JavaScript del frontend |
| [Daniel Jurado](https://github.com/daniel2722d) | Páginas HTML |

Mantenimiento y versión 2.0: Alexander Mejía Ospina.

[Portafolio](https://alexanderm-code.github.io/) · [LinkedIn](https://www.linkedin.com/in/alexander-mejia-ospina)
