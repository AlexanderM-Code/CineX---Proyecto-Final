package com.example.demo.controllers;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.services.ICinemaRoomService; // Importa la interfaz
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class CinemaRoomController {

    private final ICinemaRoomService cinemaRoomService; // Depende de la interfaz

    public CinemaRoomController(ICinemaRoomService cinemaRoomService) { // Inyecta la interfaz
        this.cinemaRoomService = cinemaRoomService;
    }

    @GetMapping
    public List<CinemaRoom> getAllRooms() {
        return cinemaRoomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public CinemaRoom getRoomById(@PathVariable int id) {
        return cinemaRoomService.getRoomById(id);
    }
}
