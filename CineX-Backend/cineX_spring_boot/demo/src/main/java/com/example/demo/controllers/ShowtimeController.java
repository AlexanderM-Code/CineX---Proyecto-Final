package com.example.demo.controllers;

import com.example.demo.entities.Showtime;
import com.example.demo.services.IShowtimeService; // Importa la interfaz
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final IShowtimeService showtimeService; // Depende de la interfaz

    public ShowtimeController(IShowtimeService showtimeService) { // Inyecta la interfaz
        this.showtimeService = showtimeService;
    }

    @GetMapping
    public List<Showtime> getAllShowtimes() {
        return showtimeService.getAllShowtimes();
    }

    @GetMapping("/{id}")
    public Showtime getShowtimeById(@PathVariable int id) {
        return showtimeService.getShowtimeById(id);
    }
}
