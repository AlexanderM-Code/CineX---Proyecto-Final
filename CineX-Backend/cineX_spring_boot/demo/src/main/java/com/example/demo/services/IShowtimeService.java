package com.example.demo.services;

import com.example.demo.entities.Showtime;

import java.util.List;

public interface IShowtimeService {
    List<Showtime> findAll(Long movieId);
    Showtime findById(Long id);
    Showtime create(Showtime showtime);
    Showtime update(Long id, Showtime showtime);
    void delete(Long id);
}
