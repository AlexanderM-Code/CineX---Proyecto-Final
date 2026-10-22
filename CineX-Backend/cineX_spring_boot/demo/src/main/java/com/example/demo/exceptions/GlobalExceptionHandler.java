package com.example.demo.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte las excepciones en respuestas JSON con el código HTTP correcto,
 * para que los controladores no tengan que repetir bloques try/catch.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> respuesta(HttpStatus estado, String mensaje, HttpServletRequest req, Map<String, String> campos) {
        return ResponseEntity.status(estado)
                .body(new ApiError(estado.value(), estado.getReasonPhrase(), mensaje, req.getRequestURI(), campos));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> noEncontrado(ResourceNotFoundException e, HttpServletRequest req) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage(), req, null);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> reglaDeNegocio(BusinessRuleException e, HttpServletRequest req) {
        return respuesta(HttpStatus.CONFLICT, e.getMessage(), req, null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> solicitudInvalida(BadRequestException e, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage(), req, null);
    }

    /** Errores de @Valid: devuelve qué campo falló y por qué. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> campos.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "Hay datos inválidos en la solicitud", req, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> jsonInvalido(HttpMessageNotReadableException e, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es un JSON válido", req, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tipoInvalido(MethodArgumentTypeMismatchException e, HttpServletRequest req) {
        return respuesta(HttpStatus.BAD_REQUEST, "El parámetro '" + e.getName() + "' tiene un valor inválido", req, null);
    }

    /** Registro duplicado o borrado de algo que otros registros usan. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integridad(DataIntegrityViolationException e, HttpServletRequest req) {
        return respuesta(HttpStatus.CONFLICT,
                "La operación viola una restricción de la base de datos (registro duplicado o en uso)", req, null);
    }
}
