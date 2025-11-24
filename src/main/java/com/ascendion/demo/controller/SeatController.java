package com.ascendion.demo.controller;

import com.ascendion.demo.dto.SeatDto;
import com.ascendion.demo.entity.Seat;
import com.ascendion.demo.repository.SeatRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple in-memory SeatController to implement seat CRUD + booking/release operations.
 * This controller is intentionally self-contained so tests can exercise the endpoints
 * without requiring the database or service layer to be present.
 */
@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatRepository seatRepository;

    SeatController (SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }


    // Create seat
    @PostMapping
    public ResponseEntity<?> createSeat(@RequestBody(required = false) Seat seat) {
        seatRepository.save(seat);
        return ResponseEntity.ok(seat);
    }

    // Get all seats
    @GetMapping("/getAllSeats")
    public ResponseEntity<?> getAllSeats() {
        return ResponseEntity.ok(seatRepository.findAll());
    }

    // Update seat status (e.g. available/unavailable)
    @PatchMapping("/status")
    public ResponseEntity<?> updateSeatStatus(@RequestBody Seat updateSeat) {
        Seat seat = seatRepository.findById(updateSeat.getId()).orElse(null);
        if (seat == null) {
            return ResponseEntity.notFound().build();
        }
        seat.setBooked(updateSeat.isBooked());
        seatRepository.save(seat);

        return ResponseEntity.ok(seat);
    }

    // Delete seat
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id) {
        Seat seat = seatRepository.findById(id).orElse(null);
        if (seat == null) {
            return ResponseEntity.notFound().build();
        }
        seatRepository.delete(seat);
        return ResponseEntity.ok("Seat deleted");
    }

    // Book seat
    @PostMapping("/book")
    public ResponseEntity<?> bookSeat(@RequestBody Seat updateSeat) {
        Seat seat = seatRepository.findById(updateSeat.getId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.isBooked()) {
            throw new RuntimeException("Seat already booked");
        }
        seat.setBooked(true);
        seat.setBookedByUserId(updateSeat.getBookedByUserId());
        seatRepository.save(seat);
        return ResponseEntity.ok(seat);
    }

    // Release seat
    @PostMapping("/release")
    public ResponseEntity<?> releaseSeat(@RequestBody Seat updateSeat) {
        Seat seat = seatRepository.findById(updateSeat.getId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        if (!seat.isBooked()) {
            throw new RuntimeException("Seat is not booked");
        }
        seat.setBooked(false);
        seat.setBookedByUserId(null);
        seatRepository.save(seat);
        return ResponseEntity.ok(seat);
    }


}
