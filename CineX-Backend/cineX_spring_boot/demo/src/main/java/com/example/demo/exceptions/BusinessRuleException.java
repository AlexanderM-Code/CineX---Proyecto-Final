package com.example.demo.exceptions;

/** La solicitud es válida pero rompe una regla del negocio (por ejemplo, sala llena). Se responde con 409. */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String mensaje) {
        super(mensaje);
    }
}
