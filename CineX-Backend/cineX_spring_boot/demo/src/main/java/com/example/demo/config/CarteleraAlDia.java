package com.example.demo.config;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.entities.Movie;
import com.example.demo.entities.Showtime;
import com.example.demo.repositories.CinemaRoomRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

/**
 * Mantiene la cartelera de demostración siempre al día: programa funciones
 * para los próximos días, así la API publicada nunca muestra solo funciones pasadas.
 * Se ejecuta al arrancar y todos los días a las 3:00 a. m. (hora de Colombia).
 * No borra funciones antiguas, porque pueden tener compras asociadas.
 */
@Component
@Profile("!test")
public class CarteleraAlDia {

    static final ZoneId ZONA = ZoneId.of("America/Bogota");
    static final int DIAS = 7;
    private static final int PELICULAS = 3;
    private static final LocalTime[] HORARIOS = {LocalTime.of(16, 0), LocalTime.of(19, 30), LocalTime.of(20, 0)};
    private static final BigDecimal[] PRECIOS = {new BigDecimal("12000"), new BigDecimal("15000"), new BigDecimal("15000")};

    private final MovieRepository movies;
    private final CinemaRoomRepository rooms;
    private final ShowtimeRepository showtimes;
    private final Clock reloj;

    @Autowired
    public CarteleraAlDia(MovieRepository movies, CinemaRoomRepository rooms, ShowtimeRepository showtimes) {
        this(movies, rooms, showtimes, Clock.system(ZONA));
    }

    CarteleraAlDia(MovieRepository movies, CinemaRoomRepository rooms, ShowtimeRepository showtimes, Clock reloj) {
        this.movies = movies;
        this.rooms = rooms;
        this.showtimes = showtimes;
        this.reloj = reloj;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 3 * * *", zone = "America/Bogota")
    @Transactional
    public void programarProximosDias() {
        programar();
    }

    /** Crea las funciones que falten entre hoy y los próximos días. Devuelve cuántas creó. */
    int programar() {
        List<Movie> peliculas = movies.findAll().stream()
                .sorted(Comparator.comparing(Movie::getId)).limit(PELICULAS).toList();
        List<CinemaRoom> salas = rooms.findAll().stream()
                .sorted(Comparator.comparing(CinemaRoom::getId)).toList();
        if (peliculas.isEmpty() || salas.isEmpty()) {
            return 0;
        }
        LocalDate hoy = LocalDate.now(reloj);
        int creadas = 0;
        for (int dia = 0; dia < DIAS; dia++) {
            for (int i = 0; i < peliculas.size(); i++) {
                CinemaRoom sala = salas.get(i % salas.size());
                LocalDateTime hora = hoy.plusDays(dia).atTime(HORARIOS[i % HORARIOS.length]);
                if (!showtimes.existsByRoomIdAndShowDateTime(sala.getId(), hora)) {
                    showtimes.save(new Showtime(peliculas.get(i), sala, hora, PRECIOS[i % PRECIOS.length]));
                    creadas++;
                }
            }
        }
        return creadas;
    }
}
