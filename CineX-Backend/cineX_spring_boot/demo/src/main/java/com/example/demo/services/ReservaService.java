package com.example.demo.services;

import com.example.demo.entities.Reserva;
import com.example.demo.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva create(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Reserva findById(Long id) {
        return reservaRepository.findById(id).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Reserva update(Long id, Reserva reserva) {
        Reserva existingReserva = findById(id);
        existingReserva.setUsuario(reserva.getUsuario());
        existingReserva.setProducto(reserva.getProducto());
        return reservaRepository.save(existingReserva);
    }

    public void delete(Long id) {
        reservaRepository.deleteById(id);
    }
}
