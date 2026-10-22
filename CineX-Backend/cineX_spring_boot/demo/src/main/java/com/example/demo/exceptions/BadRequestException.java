package com.example.demo.exceptions;

/** Faltan datos o están mal formados. Se responde con 400. */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
