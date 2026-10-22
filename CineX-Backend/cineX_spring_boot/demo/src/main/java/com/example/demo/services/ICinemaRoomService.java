package com.example.demo.services;

import com.example.demo.entities.CinemaRoom;

import java.util.List;

public interface ICinemaRoomService {
    List<CinemaRoom> findAll();
    CinemaRoom findById(Long id);
    CinemaRoom create(CinemaRoom room);
    CinemaRoom update(Long id, CinemaRoom room);
    void delete(Long id);
}
