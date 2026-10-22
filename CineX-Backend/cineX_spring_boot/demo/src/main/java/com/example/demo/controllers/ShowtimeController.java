package com.example.demo.controllers;

import com.example.demo.entities.Showtime;
import com.example.demo.services.IBookingService;
import com.example.demo.services.IShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
@Tag(name = "Funciones", description = "Películas programadas en una sala a una hora")
public class ShowtimeController {

    private final IShowtimeService showtimeService;
    private final IBookingService bookingService;

    public ShowtimeController(IShowtimeService showtimeService, IBookingService bookingService) {
        this.showtimeService = showtimeService;
        this.bookingService = bookingService;
    }

    @GetMapping
    @Operation(summary = "Listar funciones", description = "Con ?movieId=1 devuelve solo las funciones de esa película")
    public List<Showtime> findAll(@RequestParam(required = false) Long movieId) {
        return showtimeService.findAll(movieId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una función")
    public Showtime findById(@PathVariable Long id) {
        return showtimeService.findById(id);
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Sillas disponibles de una función")
    public IBookingService.Availability availability(@PathVariable Long id) {
        return bookingService.availability(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Programar una función",
            description = "Cuerpo: {\"movie\":{\"id\":1},\"room\":{\"id\":1},\"showDateTime\":\"2026-11-01T18:00:00\",\"price\":15000}. "
                    + "Falla con 409 si la sala ya tiene una función a esa hora")
    public Showtime create(@Valid @RequestBody Showtime showtime) {
        return showtimeService.create(showtime);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una función")
    public Showtime update(@PathVariable Long id, @Valid @RequestBody Showtime showtime) {
        return showtimeService.update(id, showtime);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una función", description = "Falla con 409 si la función ya tiene reservas")
    public void delete(@PathVariable Long id) {
        showtimeService.delete(id);
    }
}
