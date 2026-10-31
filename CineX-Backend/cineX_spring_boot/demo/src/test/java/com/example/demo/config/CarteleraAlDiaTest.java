package com.example.demo.config;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.entities.Movie;
import com.example.demo.repositories.CinemaRoomRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.ShowtimeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

/** La cartelera de demostración siempre tiene funciones en los próximos días. */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CarteleraAlDiaTest {

    private static final LocalDate HOY = LocalDate.of(2030, 3, 10);

    @Autowired private MovieRepository movies;
    @Autowired private CinemaRoomRepository rooms;
    @Autowired private ShowtimeRepository showtimes;

    private CarteleraAlDia cartelera() {
        Clock reloj = Clock.fixed(HOY.atStartOfDay(CarteleraAlDia.ZONA).toInstant(), ZoneId.of("UTC"));
        return new CarteleraAlDia(movies, rooms, showtimes, reloj);
    }

    @Test
    void programaFuncionesParaLosProximosDiasSinDuplicar() {
        movies.save(new Movie(1L, "Película A", "Drama", null, null, 100));
        movies.save(new Movie(2L, "Película B", "Drama", null, null, 100));
        rooms.save(new CinemaRoom("Sala A", 50));

        assertThat(cartelera().programar()).isEqualTo(2 * CarteleraAlDia.DIAS);
        assertThat(cartelera().programar()).isZero();
        assertThat(showtimes.findAll())
                .allMatch(f -> !f.getShowDateTime().toLocalDate().isBefore(HOY));
    }

    @Test
    void sinPeliculasNoHaceNada() {
        rooms.save(new CinemaRoom("Sala B", 50));
        assertThat(cartelera().programar()).isZero();
    }
}
