package com.example.demo.controllers;

import com.example.demo.entities.Movie;
import com.example.demo.services.IMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Tag(name = "Películas", description = "Cartelera del cine")
public class MovieController {

    private final IMovieService movieService;

    public MovieController(IMovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Operation(summary = "Listar películas")
    public List<Movie> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una película")
    public Movie findById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una película")
    public Movie create(@Valid @RequestBody Movie movie) {
        return movieService.create(movie);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una película")
    public Movie update(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        return movieService.update(id, movie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una película", description = "Falla con 409 si la película tiene funciones programadas")
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }
}
