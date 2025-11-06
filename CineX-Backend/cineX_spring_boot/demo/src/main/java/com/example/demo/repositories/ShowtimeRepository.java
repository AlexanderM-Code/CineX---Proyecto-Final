package com.example.demo.repositories;

import com.example.demo.entities.Showtime;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ShowtimeRepository {
    private final List<Showtime> showtimes = new ArrayList<>();

    public ShowtimeRepository() {
        // Sample data
        showtimes.add(new Showtime(1, "tt0111161", 1, LocalDateTime.now().plusHours(2)));
        showtimes.add(new Showtime(2, "tt0068646", 2, LocalDateTime.now().plusHours(3)));
    }

    public List<Showtime> findAll() {
        return showtimes;
    }

    public Optional<Showtime> findById(int id) {
        return showtimes.stream().filter(showtime -> showtime.getId() == id).findFirst();
    }
}
