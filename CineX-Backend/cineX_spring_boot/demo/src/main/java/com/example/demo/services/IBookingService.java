package com.example.demo.services;

import com.example.demo.entities.Booking;
import java.util.List;

public interface IBookingService {
    List<Booking> getAllBookings();
    Booking getBookingById(int id);
    Booking createBooking(Booking booking);
}
