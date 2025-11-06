package com.example.demo.repositories;

import com.example.demo.entities.CinemaRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CinemaRoomRepository {
    private final List<CinemaRoom> rooms = new ArrayList<>();

    public CinemaRoomRepository() {
        // Sample data
        rooms.add(new CinemaRoom(1, "Sala 1", 50));
        rooms.add(new CinemaRoom(2, "Sala 2 VIP", 25));
    }

    public List<CinemaRoom> findAll() {
        return rooms;
    }

    public Optional<CinemaRoom> findById(int id) {
        return rooms.stream().filter(room -> room.getId() == id).findFirst();
    }
}
