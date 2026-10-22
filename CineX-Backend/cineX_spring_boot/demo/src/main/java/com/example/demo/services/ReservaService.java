package com.example.demo.services;

import com.example.demo.entities.Reserva;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Reservas de productos de confitería hechas por un usuario para una película de TMDB. */
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public ReservaService(ReservaRepository reservaRepository, UsuarioService usuarioService, ProductoService productoService) {
        this.reservaRepository = reservaRepository;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @Transactional
    public Reserva create(Reserva reserva) {
        reserva.setId(null);
        completarRelaciones(reserva, reserva);
        return reservaRepository.save(reserva);
    }

    public Reserva findById(Long id) {
        return reservaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reserva", id));
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @Transactional
    public Reserva update(Long id, Reserva datos) {
        Reserva existente = findById(id);
        completarRelaciones(existente, datos);
        existente.setTmdbMovieId(datos.getTmdbMovieId());
        existente.setMovieTitle(datos.getMovieTitle());
        return reservaRepository.save(existente);
    }

    @Transactional
    public void delete(Long id) {
        reservaRepository.delete(findById(id));
    }

    /** Reemplaza las referencias que llegan solo con id por los registros reales (404 si no existen). */
    private void completarRelaciones(Reserva destino, Reserva datos) {
        destino.setUsuario(datos.getUsuario() == null || datos.getUsuario().getId() == null
                ? null : usuarioService.findById(datos.getUsuario().getId()));
        destino.setProducto(datos.getProducto() == null || datos.getProducto().getId() == null
                ? null : productoService.findById(datos.getProducto().getId()));
    }
}
