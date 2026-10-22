package com.example.demo.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato único de los errores de la API.
 * Ejemplo: {"estado":409,"error":"Conflict","mensaje":"Solo quedan 3 sillas...","ruta":"/bookings"}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        int estado,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> campos,
        LocalDateTime fecha
) {
    public ApiError(int estado, String error, String mensaje, String ruta, Map<String, String> campos) {
        this(estado, error, mensaje, ruta, campos, LocalDateTime.now());
    }
}
