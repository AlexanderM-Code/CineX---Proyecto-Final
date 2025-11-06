package com.example.demo.services;

import com.example.demo.entities.CinemaRoom;
import java.util.List;

public interface ICinemaRoomService {
    List<CinemaRoom> getAllRooms();
    CinemaRoom getRoomById(int id);
}
