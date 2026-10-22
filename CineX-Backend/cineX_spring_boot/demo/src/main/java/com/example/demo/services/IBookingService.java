package com.example.demo.services;

import com.example.demo.entities.Booking;

import java.util.List;

public interface IBookingService {
    List<Booking> findAll(Long showtimeId);
    Booking findById(Long id);
    Booking create(Booking booking);
    Booking update(Long id, Booking booking);
    void delete(Long id);
    Availability availability(Long showtimeId);

    /** Sillas de una función: capacidad de la sala, vendidas y disponibles. */
    record Availability(Long showtimeId, int capacidad, long vendidas, long disponibles) {}
}
