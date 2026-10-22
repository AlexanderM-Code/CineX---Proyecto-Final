package com.example.demo.services;

import com.example.demo.entities.Booking;
import com.example.demo.entities.Showtime;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final IShowtimeService showtimeService;

    public BookingService(BookingRepository bookingRepository, IShowtimeService showtimeService) {
        this.bookingRepository = bookingRepository;
        this.showtimeService = showtimeService;
    }

    @Override
    public List<Booking> findAll(Long showtimeId) {
        return showtimeId == null ? bookingRepository.findAll() : bookingRepository.findByShowtimeId(showtimeId);
    }

    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reserva de sillas", id));
    }

    /**
     * Regla principal: no se pueden vender más sillas que la capacidad de la sala.
     */
    @Override
    @Transactional
    public Booking create(Booking booking) {
        if (booking.getShowtime() == null || booking.getShowtime().getId() == null) {
            throw new BadRequestException("El campo 'showtime.id' es obligatorio");
        }
        Showtime showtime = showtimeService.findById(booking.getShowtime().getId());
        validarCupo(showtime, booking.getNumberOfSeats(), 0);

        booking.setId(null);
        booking.setShowtime(showtime);
        return bookingRepository.save(booking);
    }

    /** Permite cambiar el correo y el número de sillas; la función no cambia. */
    @Override
    @Transactional
    public Booking update(Long id, Booking datos) {
        Booking booking = findById(id);
        validarCupo(booking.getShowtime(), datos.getNumberOfSeats(), booking.getNumberOfSeats());
        booking.setCustomerEmail(datos.getCustomerEmail());
        booking.setNumberOfSeats(datos.getNumberOfSeats());
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookingRepository.delete(findById(id));
    }

    @Override
    public Availability availability(Long showtimeId) {
        Showtime showtime = showtimeService.findById(showtimeId);
        int capacidad = showtime.getRoom().getCapacity();
        long vendidas = bookingRepository.sumSeatsByShowtime(showtimeId);
        return new Availability(showtimeId, capacidad, vendidas, capacidad - vendidas);
    }

    /**
     * @param sillasPedidas  sillas de la compra nueva o editada
     * @param sillasPropias  sillas que esta misma reserva ya tenía (0 si es nueva)
     */
    private void validarCupo(Showtime showtime, int sillasPedidas, int sillasPropias) {
        long vendidas = bookingRepository.sumSeatsByShowtime(showtime.getId()) - sillasPropias;
        long disponibles = showtime.getRoom().getCapacity() - vendidas;
        if (sillasPedidas > disponibles) {
            throw new BusinessRuleException("Solo quedan " + disponibles + " sillas disponibles para esta función");
        }
    }
}
