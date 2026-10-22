package com.example.demo.exceptions;

/** El recurso pedido no existe. Se responde con 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String recurso, Object id) {
        super(recurso + " con id " + id + " no existe");
    }
}
