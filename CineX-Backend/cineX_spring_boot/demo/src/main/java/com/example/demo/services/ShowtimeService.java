package com.example.demo.services;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.entities.Movie;
import com.example.demo.entities.Showtime;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.ShowtimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShowtimeService implements IShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final BookingRepository bookingRepository;
    private final IMovieService movieService;
    private final ICinemaRoomService roomService;

    public ShowtimeService(ShowtimeRepository showtimeRepository, BookingRepository bookingRepository,
                           IMovieService movieService, ICinemaRoomService roomService) {
        this.showtimeRepository = showtimeRepository;
        this.bookingRepository = bookingRepository;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    /** Todas las funciones, o solo las de una película si se indica movieId. */
    @Override
    public List<Showtime> findAll(Long movieId) {
        return movieId == null
                ? showtimeRepository.findAll()
                : showtimeRepository.findByMovieIdOrderByShowDateTimeAsc(movieId);
    }

    @Override
    public Showtime findById(Long id) {
        return showtimeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Función", id));
    }

    @Override
    @Transactional
    public Showtime create(Showtime showtime) {
        Movie movie = movieService.findById(idObligatorio(showtime.getMovie() == null ? null : showtime.getMovie().getId(), "movie.id"));
        CinemaRoom room = roomService.findById(idObligatorio(showtime.getRoom() == null ? null : showtime.getRoom().getId(), "room.id"));

        if (showtimeRepository.existsByRoomIdAndShowDateTime(room.getId(), showtime.getShowDateTime())) {
            throw salaOcupada(room, showtime);
        }
        showtime.setId(null);
        showtime.setMovie(movie);
        showtime.setRoom(room);
        return showtimeRepository.save(showtime);
    }

    @Override
    @Transactional
    public Showtime update(Long id, Showtime datos) {
        Showtime showtime = findById(id);
        Movie movie = movieService.findById(idObligatorio(datos.getMovie() == null ? null : datos.getMovie().getId(), "movie.id"));
        CinemaRoom room = roomService.findById(idObligatorio(datos.getRoom() == null ? null : datos.getRoom().getId(), "room.id"));

        if (showtimeRepository.existsByRoomIdAndShowDateTimeAndIdNot(room.getId(), datos.getShowDateTime(), id)) {
            throw salaOcupada(room, datos);
        }
        long vendidas = bookingRepository.sumSeatsByShowtime(id);
        if (room.getCapacity() < vendidas) {
            throw new BusinessRuleException("La sala \"" + room.getName() + "\" tiene " + room.getCapacity()
                    + " sillas, pero esta función ya vendió " + vendidas);
        }
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setShowDateTime(datos.getShowDateTime());
        showtime.setPrice(datos.getPrice());
        return showtimeRepository.save(showtime);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Showtime showtime = findById(id);
        if (bookingRepository.existsByShowtimeId(id)) {
            throw new BusinessRuleException("No se puede eliminar la función porque ya tiene reservas");
        }
        showtimeRepository.delete(showtime);
    }

    private static Long idObligatorio(Long id, String campo) {
        if (id == null) {
            throw new BadRequestException("El campo '" + campo + "' es obligatorio");
        }
        return id;
    }

    private static BusinessRuleException salaOcupada(CinemaRoom room, Showtime showtime) {
        return new BusinessRuleException("La sala \"" + room.getName() + "\" ya tiene una función el "
                + showtime.getShowDateTime());
    }
}
