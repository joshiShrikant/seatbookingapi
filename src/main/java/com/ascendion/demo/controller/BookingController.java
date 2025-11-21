package com.ascendion.demo.controller;

import com.ascendion.demo.dto.BookingRequest;
import com.ascendion.demo.dto.BookingResponse;
import com.ascendion.demo.dto.ReleaseRequest;
import com.ascendion.demo.dto.UpdateBookingRequest;
import com.ascendion.demo.service.BookingService;
import com.ascendion.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/getMyBookings")
    public ResponseEntity<?> myBookings( @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdFromUserName(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @PostMapping("/bookMySeat")
    public BookingResponse bookMySeat(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookingRequest request) {

        Long userId = userService.getUserIdFromUserName(userDetails.getUsername());
        return bookingService.createBooking(userId, request);
    }

    @PostMapping("releaseMySeat")
    public ResponseEntity<?> releaseMySeat( @AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody ReleaseRequest request) {
        // Implementation to release a booked seat
        Long userId = userService.getUserIdFromUserName(userDetails.getUsername());
        return bookingService.releaseBooking(userId, request);
    }

    @PostMapping("/updateMyBooking")
    public BookingResponse updateMyBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateBookingRequest request) {
        Long userId = userService.getUserIdFromUserName(userDetails.getUsername());
        return bookingService.updateBooking(userId, request);
    }


    //    /seats/floor  Get seats list for a floor

    @GetMapping("/seats/floor")
    public ResponseEntity<?> getFloors() {
        return ResponseEntity.ok(bookingService.getFloors());
    }

//    /seats/floor/{floorId}  Get seats list for a floor

    @GetMapping("/seats/floor/{floorId}")
    public ResponseEntity<?> getSeatsByFloor(@PathVariable Long floorId) {
        return ResponseEntity.ok(bookingService.getSeatsByFloor(floorId));
    }


}

