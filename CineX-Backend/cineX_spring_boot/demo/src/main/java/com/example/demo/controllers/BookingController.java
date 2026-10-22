package com.example.demo.controllers;

import com.example.demo.entities.Booking;
import com.example.demo.services.IBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Compra de sillas", description = "Reserva de sillas para una función")
public class BookingController {

    private final IBookingService bookingService;

    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @Operation(summary = "Listar compras", description = "Con ?showtimeId=1 devuelve solo las de esa función")
    public List<Booking> findAll(@RequestParam(required = false) Long showtimeId) {
        return bookingService.findAll(showtimeId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una compra")
    public Booking findById(@PathVariable Long id) {
        return bookingService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Comprar sillas",
            description = "Cuerpo: {\"showtime\":{\"id\":1},\"customerEmail\":\"ana@correo.com\",\"numberOfSeats\":2}. "
                    + "Falla con 409 si no quedan suficientes sillas")
    public Booking create(@Valid @RequestBody Booking booking) {
        return bookingService.create(booking);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cambiar el correo o el número de sillas de una compra")
    public Booking update(@PathVariable Long id, @Valid @RequestBody Booking booking) {
        return bookingService.update(id, booking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancelar una compra")
    public void delete(@PathVariable Long id) {
        bookingService.delete(id);
    }
}
