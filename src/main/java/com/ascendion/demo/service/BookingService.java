package com.ascendion.demo.service;

import com.ascendion.demo.dto.BookingRequest;
import com.ascendion.demo.dto.BookingResponse;
import com.ascendion.demo.dto.ReleaseRequest;
import com.ascendion.demo.dto.UpdateBookingRequest;
import com.ascendion.demo.entity.Floor;
import com.ascendion.demo.entity.Seat;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingRequest request);

    List<BookingResponse> getUserBookings(Long userId);

    BookingResponse bookSeat(Long userId, BookingRequest request);

    ResponseEntity<?> releaseBooking(Long userId, ReleaseRequest request);

    BookingResponse updateBooking(Long userId, UpdateBookingRequest request);

    List<Seat> getSeatsByFloor(Long floorId);

    List<Floor> getFloors();
}
