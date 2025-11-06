package com.example.demo.controllers;

import com.example.demo.entities.Movie;
import com.example.demo.services.IMovieService; // Importa la interfaz
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final IMovieService movieService; // Depende de la interfaz

    public MovieController(IMovieService movieService) { // Inyecta la interfaz
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable String id) {
        return movieService.getMovieById(id);
    }

    // NUEVO ENDPOINT PARA CREAR UNA PELÍCULA
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }
}
