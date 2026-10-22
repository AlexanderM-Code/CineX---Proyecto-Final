package com.example.demo.services;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.CinemaRoomRepository;
import com.example.demo.repositories.ShowtimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CinemaRoomService implements ICinemaRoomService {

    private final CinemaRoomRepository roomRepository;
    private final ShowtimeRepository showtimeRepository;

    public CinemaRoomService(CinemaRoomRepository roomRepository, ShowtimeRepository showtimeRepository) {
        this.roomRepository = roomRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public List<CinemaRoom> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public CinemaRoom findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sala", id));
    }

    @Override
    @Transactional
    public CinemaRoom create(CinemaRoom room) {
        if (roomRepository.existsByNameIgnoreCase(room.getName())) {
            throw new BusinessRuleException("Ya existe una sala llamada \"" + room.getName() + "\"");
        }
        room.setId(null);
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public CinemaRoom update(Long id, CinemaRoom datos) {
        CinemaRoom room = findById(id);
        if (roomRepository.existsByNameIgnoreCaseAndIdNot(datos.getName(), id)) {
            throw new BusinessRuleException("Ya existe una sala llamada \"" + datos.getName() + "\"");
        }
        room.setName(datos.getName());
        room.setCapacity(datos.getCapacity());
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CinemaRoom room = findById(id);
        if (showtimeRepository.existsByRoomId(id)) {
            throw new BusinessRuleException("No se puede eliminar la sala \"" + room.getName() + "\" porque tiene funciones programadas");
        }
        roomRepository.delete(room);
    }
}
