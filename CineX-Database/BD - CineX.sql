-- =====================================================================
-- CineX: esquema de la base de datos (MySQL 8)
-- Coincide con las entidades JPA del backend. Lo usa Docker Compose al
-- crear la base por primera vez; la aplicación carga datos de ejemplo.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS cinex_db;
USE cinex_db;

-- Clientes registrados en el panel de administración
CREATE TABLE IF NOT EXISTS usuario (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email  VARCHAR(255) NOT NULL UNIQUE
);

-- Productos de confitería
CREATE TABLE IF NOT EXISTS producto (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio DOUBLE NOT NULL
);

-- Reserva de un producto por un usuario para una película de TMDB
CREATE TABLE IF NOT EXISTS reserva (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id    BIGINT,
    producto_id   BIGINT,
    tmdb_movie_id BIGINT,
    movie_title   VARCHAR(255),
    CONSTRAINT fk_reserva_usuario  FOREIGN KEY (usuario_id)  REFERENCES usuario (id),
    CONSTRAINT fk_reserva_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);

-- Cartelera
CREATE TABLE IF NOT EXISTS movie (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    tmdb_id          BIGINT UNIQUE,
    title            VARCHAR(200)  NOT NULL,
    genre            VARCHAR(80),
    description      VARCHAR(1000),
    image_url        VARCHAR(500),
    duration_minutes INT NOT NULL
);

-- Salas y su capacidad
CREATE TABLE IF NOT EXISTS cinema_room (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(60) NOT NULL UNIQUE,
    capacity INT NOT NULL
);

-- Funciones: una película en una sala a una hora (una sala no puede tener dos a la misma hora)
CREATE TABLE IF NOT EXISTS showtime (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id       BIGINT NOT NULL,
    room_id        BIGINT NOT NULL,
    show_date_time DATETIME(6) NOT NULL,
    price          DECIMAL(10, 2) NOT NULL,
    CONSTRAINT uk_showtime_room_time UNIQUE (room_id, show_date_time),
    CONSTRAINT fk_showtime_movie FOREIGN KEY (movie_id) REFERENCES movie (id),
    CONSTRAINT fk_showtime_room  FOREIGN KEY (room_id)  REFERENCES cinema_room (id)
);

-- Compra de sillas para una función
CREATE TABLE IF NOT EXISTS booking (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    showtime_id     BIGINT NOT NULL,
    customer_email  VARCHAR(120) NOT NULL,
    number_of_seats INT NOT NULL,
    created_at      DATETIME(6) NOT NULL,
    CONSTRAINT fk_booking_showtime FOREIGN KEY (showtime_id) REFERENCES showtime (id)
);

-- Usuarios de ejemplo (el equipo del proyecto)
INSERT INTO usuario (nombre, email) VALUES
('Alexander', 'alexander@example.com'),
('SantiagoM', 'santiagom@example.com'),
('Daniel', 'daniel@example.com'),
('Ronald', 'ronald@example.com'),
('SantiagoR', 'santiagor@example.com');
