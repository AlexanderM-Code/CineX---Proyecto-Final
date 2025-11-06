package com.example.demo.services;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.repositories.CinemaRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaRoomService implements ICinemaRoomService { // Implementa la interfaz

    private final CinemaRoomRepository cinemaRoomRepository;

    public CinemaRoomService(CinemaRoomRepository cinemaRoomRepository) {
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    @Override
    public List<CinemaRoom> getAllRooms() {
        return cinemaRoomRepository.findAll();
    }

    @Override
    public CinemaRoom getRoomById(int id) {
        return cinemaRoomRepository.findById(id).orElse(null);
    }
}
