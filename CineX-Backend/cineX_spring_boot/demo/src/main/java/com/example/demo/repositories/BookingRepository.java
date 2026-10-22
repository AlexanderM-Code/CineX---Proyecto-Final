package com.example.demo.repositories;

import com.example.demo.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByShowtimeId(Long showtimeId);

    boolean existsByShowtimeId(Long showtimeId);

    /** Total de sillas vendidas en una función (0 si no hay reservas). */
    @Query("select coalesce(sum(b.numberOfSeats), 0L) from Booking b where b.showtime.id = :showtimeId")
    long sumSeatsByShowtime(@Param("showtimeId") Long showtimeId);
}
