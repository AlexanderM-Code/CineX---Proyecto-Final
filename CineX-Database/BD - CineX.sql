-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS cinex_db;

-- Usar la base de datos recién creada
USE cinex_db;

-- Crear la tabla 'usuario'
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Crear la tabla 'producto'
CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio DOUBLE NOT NULL
);

-- Crear la tabla 'reserva'
CREATE TABLE IF NOT EXISTS reserva (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT,
    producto_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Insertar los datos de ejemplo con los nuevos nombres
INSERT INTO usuario (nombre, email) VALUES 
('Alexander', 'alexander@example.com'),
('SantiagoM', 'santiagom@example.com'),
('Daniel', 'daniel@example.com'),
('Ronald', 'ronald@example.com'),
('SantiagoR', 'santiagor@example.com');

INSERT INTO producto (nombre, precio) VALUES 
('Entrada General', 75.50),
('Combo Palomitas + Refresco', 120.00);