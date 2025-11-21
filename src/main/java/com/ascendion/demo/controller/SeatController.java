package com.ascendion.demo.controller;

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

    // simple in-memory store: id -> Seat
    private final Map<Long, Seat> seats = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // --- DTO ---
    public static class Seat {
        public Long id;
        public String code;
        public String status; // e.g. "available", "booked", "unavailable"
        public Long bookedByUserId;
        public Instant bookedAt;

        public Seat() { }

        public Seat(Long id, String code, String status) {
            this.id = id;
            this.code = code;
            this.status = status;
        }
    }

    // Create seat
    @PostMapping
    public ResponseEntity<?> createSeat(@RequestBody(required = false) Seat input) {
        Long id = idGenerator.getAndIncrement();
        String code = (input != null && input.code != null) ? input.code : "S-" + id;
        String status = (input != null && input.status != null) ? input.status : "available";
        Seat seat = new Seat(id, code, status);
        seats.put(id, seat);
        return ResponseEntity.ok(seat);
    }

    // Get all seats
    @GetMapping("/getAllSeats")
    public ResponseEntity<?> getAllSeats() {
        return ResponseEntity.ok(new ArrayList<>(seats.values()));
    }

    // Update seat status (e.g. available/unavailable)
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateSeatStatus(@PathVariable Long id, @RequestParam String status) {
        Seat seat = seats.get(id);
        if (seat == null) {
            return ResponseEntity.notFound().build();
        }
        seat.status = status;
        // if status becomes available, clear booking meta
        if (!"booked".equalsIgnoreCase(status)) {
            seat.bookedByUserId = null;
            seat.bookedAt = null;
        }
        seats.put(id, seat);
        return ResponseEntity.ok(seat);
    }

    // Delete seat
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id) {
        Seat removed = seats.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Seat deleted");
    }

    // Book seat
    @PostMapping("/{id}/book")
    public ResponseEntity<?> bookSeat(@PathVariable Long id, @RequestParam Long userId) {
        Seat seat = seats.get(id);
        if (seat == null) {
            return ResponseEntity.notFound().build();
        }
        if ("booked".equalsIgnoreCase(seat.status)) {
            return ResponseEntity.badRequest().body("Seat already booked");
        }
        seat.status = "booked";
        seat.bookedByUserId = userId;
        seat.bookedAt = Instant.now();
        seats.put(id, seat);
        return ResponseEntity.ok(seat);
    }

    // Release seat
    @PostMapping("/{id}/release")
    public ResponseEntity<?> releaseSeat(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        Seat seat = seats.get(id);
        if (seat == null) {
            return ResponseEntity.notFound().build();
        }
        if (!"booked".equalsIgnoreCase(seat.status)) {
            return ResponseEntity.badRequest().body("Seat is not booked");
        }
        // optional check: if userId provided, ensure it matches bookedByUserId
        if (userId != null && seat.bookedByUserId != null && !userId.equals(seat.bookedByUserId)) {
            return ResponseEntity.status(403).body("Seat booked by a different user");
        }
        seat.status = "available";
        seat.bookedByUserId = null;
        seat.bookedAt = null;
        seats.put(id, seat);
        return ResponseEntity.ok(seat);
    }


}
