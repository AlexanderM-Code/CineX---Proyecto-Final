package com.example.demo.services;

import com.example.demo.entities.Booking;
import com.example.demo.entities.CinemaRoom;
import com.example.demo.entities.Movie;
import com.example.demo.entities.Showtime;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.repositories.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Prueba unitaria de la regla de capacidad, sin base de datos (con Mockito). */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private IShowtimeService showtimeService;

    @InjectMocks
    private BookingService bookingService;

    private Showtime funcion;

    @BeforeEach
    void funcionEnSalaDeCincoSillas() {
        funcion = new Showtime(new Movie(null, "Encanto", null, null, null, 102),
                new CinemaRoom("Sala 3", 5), LocalDateTime.of(2030, 1, 1, 18, 0), new BigDecimal("12000"));
        funcion.setId(7L);
        when(showtimeService.findById(7L)).thenReturn(funcion);
    }

    private Booking compra(int sillas) {
        Showtime referencia = new Showtime();
        referencia.setId(7L);
        return new Booking(referencia, "ana@correo.com", sillas);
    }

    @Test
    void guardaLaCompraSiHayCupo() {
        when(bookingRepository.sumSeatsByShowtime(7L)).thenReturn(3L);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking guardada = bookingService.create(compra(2));

        assertThat(guardada.getShowtime()).isSameAs(funcion);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void rechazaLaCompraSiSuperaLaCapacidad() {
        when(bookingRepository.sumSeatsByShowtime(7L)).thenReturn(4L);

        assertThatThrownBy(() -> bookingService.create(compra(2)))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Solo quedan 1 sillas disponibles para esta función");
        verify(bookingRepository, never()).save(any());
    }
}
