package com.example.demo.controllers;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.services.ICinemaRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@Tag(name = "Salas", description = "Salas del cine y su capacidad")
public class CinemaRoomController {

    private final ICinemaRoomService roomService;

    public CinemaRoomController(ICinemaRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @Operation(summary = "Listar salas")
    public List<CinemaRoom> findAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una sala")
    public CinemaRoom findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una sala", description = "Falla con 409 si ya existe una sala con ese nombre")
    public CinemaRoom create(@Valid @RequestBody CinemaRoom room) {
        return roomService.create(room);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sala")
    public CinemaRoom update(@PathVariable Long id, @Valid @RequestBody CinemaRoom room) {
        return roomService.update(id, room);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una sala", description = "Falla con 409 si la sala tiene funciones programadas")
    public void delete(@PathVariable Long id) {
        roomService.delete(id);
    }
}
