package com.example.demo.repositories;

import com.example.demo.entities.Booking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    public List<Booking> findAll() {
        return bookings;
    }

    public Optional<Booking> findById(int id) {
        return bookings.stream().filter(booking -> booking.getId() == id).findFirst();
    }

    public Booking save(Booking booking) {
        booking.setId(counter.incrementAndGet());
        bookings.add(booking);
        return booking;
    }
}
