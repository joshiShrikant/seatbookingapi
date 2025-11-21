package com.ascendion.demo.controller;

import com.ascendion.demo.dto.SignupRequest;
import com.ascendion.demo.dto.UpdateUserRequest;
import com.ascendion.demo.entity.Seat;
import com.ascendion.demo.entity.User;
import com.ascendion.demo.repository.SeatRepository;
import com.ascendion.demo.repository.UserRepository;
import com.ascendion.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    public final UserRepository userRepository;
    public final UserService userService;
    public final SeatRepository seatRepository;

    public AdminController(UserRepository userRepository, UserService userService, SeatRepository seatRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/users/getAll")
    public ResponseEntity<?> getAllUsers() {
        // Implementation to get all users
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/getUserById")
    public ResponseEntity<?> getUserById(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // Implementation to get user by ID
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/create")
    public ResponseEntity<?> createUser(@RequestBody SignupRequest req) {
        return ResponseEntity.ok(userService.register(req));
    }

    @PostMapping("/users/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest req) {
        User user = userRepository.findById(req.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserName(req.getUserName());
        user.setEmail(req.getEmail());
        userRepository.save(user);
        // Implementation to update a user
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/users/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        // Implementation to delete a user
        return ResponseEntity.ok("User deleted successfully with user Id" + userId);
    }

    //Manage Seats	Add, edit, delete seats

    @PostMapping("/seats/addSeat")
    public ResponseEntity<?> addSeat(@RequestBody Seat request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Seat request body required");
        }
        if (request.getFloor() == null) {
            return ResponseEntity.badRequest().body("Seat.floor is required");
        }
        if (request.getSeatNumber() == null || request.getSeatNumber().isBlank()) {
            return ResponseEntity.badRequest().body("seatNumber is required");
        }
        Seat saved = seatRepository.save(request);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/seats/updateSeat")
    public ResponseEntity<?> updateSeat(@RequestBody Seat request) {
        if (request == null || request.getId() == null) {
            return ResponseEntity.badRequest().body("Seat id is required for update");
        }
        Seat existing = seatRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Seat not found"));
        // update allowed fields
        existing.setSeatNumber(request.getSeatNumber() == null ? existing.getSeatNumber() : request.getSeatNumber());
        existing.setXCoordinate(request.getXCoordinate() == null ? existing.getXCoordinate() : request.getXCoordinate());
        existing.setYCoordinate(request.getYCoordinate() == null ? existing.getYCoordinate() : request.getYCoordinate());
        existing.setFloor(request.getFloor() == null ? existing.getFloor() : request.getFloor());
        existing.setActive(request.isActive());
        Seat saved = seatRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/seats/deleteSeat")
    public ResponseEntity<?> deleteSeats(@RequestParam Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));
        seatRepository.delete(seat);
        return ResponseEntity.ok("Seat deleted successfully with id " + seatId);
    }

    @PostMapping("/admin/floors/addFloor")
    public ResponseEntity<?> addFloor(@RequestParam Integer floorNumber) {
        // Implementation to add a floor
        return ResponseEntity.ok("Floor " + floorNumber + " added successfully");
    }

    @PostMapping("/admin/floors/RemoveFloor")
    public ResponseEntity<?> removeFloor(@RequestParam Integer floorNumber) {
        // Implementation to remove a floor
        return ResponseEntity.ok("Floor " + floorNumber + " removed successfully");
    }


    @PostMapping("/admin/floors/addSeatToFloor")
    public ResponseEntity<?> mapSeatsToFloor(@RequestParam Integer floorNumber, @RequestBody Seat[] seats) {
        // Implementation to map seats to a floor
        return ResponseEntity.ok("Mapped " + seats.length + " seats to floor " + floorNumber);
    }
    @GetMapping("/admin/utilization")
    public ResponseEntity<?> viewUtilization(@RequestParam String period) {
        // Implementation to view utilization stats
        return ResponseEntity.ok("Utilization stats for period: " + period);
    }
    @GetMapping("/admin/reports/export")
    public ResponseEntity<?> exportReports(@RequestParam String format) {
        // Implementation to export reports
        return ResponseEntity.ok("Reports exported in format: " + format);
    }

}


//Feature	Description
//Admin Login	Protected admin routes
//Manage Seats	Add, edit, delete seats
//Manage Floors	Add floors + seat mapping (rows, columns)
//View Utilization	Daily/weekly/monthly occupancy stats
//Export Reports	CSV/PDF export (optional)