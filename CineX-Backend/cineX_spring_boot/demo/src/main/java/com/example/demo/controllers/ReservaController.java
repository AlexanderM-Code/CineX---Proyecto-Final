package com.example.demo.controllers;

import com.example.demo.entities.Reserva;
import com.example.demo.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Reservas de confitería para una película de TMDB")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Listar reservas")
    public List<Reserva> findAll() {
        return reservaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener por id")
    public Reserva findById(@PathVariable Long id) {
        return reservaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear")
    public Reserva create(@RequestBody Reserva reserva) {
        return reservaService.create(reserva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public Reserva update(@PathVariable Long id, @RequestBody Reserva reserva) {
        return reservaService.update(id, reserva);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar")
    public void delete(@PathVariable Long id) {
        reservaService.delete(id);
    }
}
