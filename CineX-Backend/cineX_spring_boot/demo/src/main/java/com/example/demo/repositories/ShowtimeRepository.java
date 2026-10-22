package com.example.demo.repositories;

import com.example.demo.entities.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    /** Funciones de una película, en orden cronológico. */
    List<Showtime> findByMovieIdOrderByShowDateTimeAsc(Long movieId);

    /** ¿La sala ya tiene una función a esa hora? */
    boolean existsByRoomIdAndShowDateTime(Long roomId, LocalDateTime showDateTime);

    /** Igual que el anterior, pero ignorando la función que se está editando. */
    boolean existsByRoomIdAndShowDateTimeAndIdNot(Long roomId, LocalDateTime showDateTime, Long id);

    boolean existsByMovieId(Long movieId);

    boolean existsByRoomId(Long roomId);
}
