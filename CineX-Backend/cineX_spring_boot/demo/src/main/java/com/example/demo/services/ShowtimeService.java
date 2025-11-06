package com.example.demo.services;

import com.example.demo.entities.Showtime;
import com.example.demo.repositories.ShowtimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService implements IShowtimeService { // Implementa la interfaz

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    @Override
    public Showtime getShowtimeById(int id) {
        return showtimeRepository.findById(id).orElse(null);
    }
}
