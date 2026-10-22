package com.example.demo.config;

import com.example.demo.entities.CinemaRoom;
import com.example.demo.entities.Movie;
import com.example.demo.entities.Producto;
import com.example.demo.entities.Showtime;
import com.example.demo.repositories.CinemaRoomRepository;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.repositories.ProductoRepository;
import com.example.demo.repositories.ShowtimeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Carga datos de ejemplo la primera vez que arranca la aplicación (base vacía).
 * No se ejecuta en las pruebas (perfil "test").
 */
@Component
@Profile("!test")
public class DatosIniciales implements CommandLineRunner {

    private final MovieRepository movies;
    private final CinemaRoomRepository rooms;
    private final ShowtimeRepository showtimes;
    private final ProductoRepository productos;

    public DatosIniciales(MovieRepository movies, CinemaRoomRepository rooms,
                          ShowtimeRepository showtimes, ProductoRepository productos) {
        this.movies = movies;
        this.rooms = rooms;
        this.showtimes = showtimes;
        this.productos = productos;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (movies.count() > 0) {
            return;
        }
        Movie suenoDeFuga = movies.save(new Movie(278L, "Sueño de fuga", "Drama",
                "Dos hombres encarcelados forjan una amistad a lo largo de los años.", null, 142));
        Movie padrino = movies.save(new Movie(238L, "El padrino", "Crimen",
                "El patriarca de una familia del crimen organizado transfiere el control a su hijo.", null, 175));
        Movie caballero = movies.save(new Movie(155L, "Batman: el caballero de la noche", "Acción",
                "Batman enfrenta al Joker, que siembra el caos en Ciudad Gótica.", null, 152));

        CinemaRoom sala1 = rooms.save(new CinemaRoom("Sala 1", 80));
        CinemaRoom sala2 = rooms.save(new CinemaRoom("Sala 2", 60));

        LocalDate manana = LocalDate.now().plusDays(1);
        showtimes.save(new Showtime(suenoDeFuga, sala1, manana.atTime(LocalTime.of(16, 0)), new BigDecimal("12000")));
        showtimes.save(new Showtime(padrino, sala1, manana.atTime(LocalTime.of(19, 30)), new BigDecimal("15000")));
        showtimes.save(new Showtime(caballero, sala2, manana.atTime(LocalTime.of(20, 0)), new BigDecimal("15000")));

        if (productos.count() == 0) {
            Producto crispetas = new Producto();
            crispetas.setNombre("Crispetas medianas");
            crispetas.setPrecio(12000);
            Producto combo = new Producto();
            combo.setNombre("Combo crispetas + gaseosa");
            combo.setPrecio(18000);
            productos.save(crispetas);
            productos.save(combo);
        }
    }
}
