package com.example.demo.controllers;

import com.example.demo.entities.Booking;
import com.example.demo.services.IBookingService; // Importa la interfaz
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final IBookingService bookingService; // Depende de la interfaz

    public BookingController(IBookingService bookingService) { // Inyecta la interfaz
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable int id) {
        return bookingService.getBookingById(id);
    }
}
