package com.example.demo.services;

import com.example.demo.entities.Showtime;
import java.util.List;

public interface IShowtimeService {
    List<Showtime> getAllShowtimes();
    Showtime getShowtimeById(int id);
}
