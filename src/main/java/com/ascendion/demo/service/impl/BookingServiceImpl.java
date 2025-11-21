package com.ascendion.demo.service.impl;

import com.ascendion.demo.dto.BookingRequest;
import com.ascendion.demo.dto.BookingResponse;
import com.ascendion.demo.dto.ReleaseRequest;
import com.ascendion.demo.dto.UpdateBookingRequest;
import com.ascendion.demo.entity.Booking;
import com.ascendion.demo.entity.Floor;
import com.ascendion.demo.entity.Seat;
import com.ascendion.demo.entity.User;
import com.ascendion.demo.repository.BookingRepository;
import com.ascendion.demo.repository.SeatRepository;
import com.ascendion.demo.repository.UserRepository;
import com.ascendion.demo.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

   public BookingServiceImpl(BookingRepository bookingRepository, SeatRepository seatRepository, UserRepository userRepository){
       this.bookingRepository = bookingRepository;
       this.seatRepository = seatRepository;
       this.userRepository = userRepository;
   }




    public BookingResponse createBooking(Long userId, BookingRequest request) {

        // 1. Check seat exists
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        // 2. Check user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Check duplicate booking
        if (bookingRepository.existsBySeatIdAndBookingDate(request.getSeatId(), request.getBookingDate())) {
            throw new RuntimeException("Seat already booked for this date");
        }

        // 4. Create booking
        Booking booking = new Booking();
        booking.setSeat(seat);
        booking.setUser(user);
        booking.setBookingTime(request.getBookingTime());
        booking.setBookingDate(request.getBookingDate());

        booking = bookingRepository.save(booking);

        // 5. Convert to response DTO
        return toResponse(booking);
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId) {

        return bookingRepository.findByUserId(userId)
                .stream()
                .map(booking -> BookingResponse.builder()
                        .id(booking.getId())
                        .seatId(booking.getSeat().getId())
                        .userId(booking.getUser().getId())
                        .bookingTime(booking.getBookingTime())
                        .createdAt(booking.getCreatedAt())
//                        .floorNumber(booking.getSeat().getFloor().getFloorNumber())
                        .status(booking.getStatus().name())
                        .bookingDate(booking.getBookingDate())
                        .build() // 👈 build the DTO here
                )
                .toList(); // 👈 close the stream correctly
    }

    @Override
    public BookingResponse bookSeat(Long userId, BookingRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<?> releaseBooking(Long userId, ReleaseRequest request) {
        // 1. Check seat exists
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        // 2. Check user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Check duplicate booking
        if (bookingRepository.existsBySeatIdAndBookingDate(request.getSeatId(), request.getBookingDate())) {
            throw new RuntimeException("Seat already booked for this date");
        }

        // 4. Release booking
        Booking booking = new Booking();
        booking.setSeat(seat);
        booking.setUser(user);
        booking.setBookingDate(request.getBookingDate());
        booking = bookingRepository.save(booking);


        // 5. Convert to response DTO
        return ResponseEntity.ok(toResponse(booking));
    }

    @Override
    public BookingResponse updateBooking(Long userId, UpdateBookingRequest request) {
        // 1. Check seat exists
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        // 2. Check user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // 3. Check duplicate booking
        if (bookingRepository.existsBySeatIdAndBookingDate(request.getSeatId(), request.getBookingDate())) {
            throw new RuntimeException("Seat already booked for this date");
        }
        // 4.  Update booking

        Booking booking = new Booking();
        booking.setId(request.getBookingId());
        booking.setSeat(seat);
        booking.setUser(user);
        booking.setBookingTime(request.getBookingTime());
        booking.setBookingDate(request.getBookingDate());

        booking = bookingRepository.save(booking);

        // 5. Convert to response DTO
        return toResponse(booking);
    }

    @Override
    public List<Seat> getSeatsByFloor(Long floorId) {
        return List.of(); // with status BOOKED or RELEASED
    }

    @Override
    public List<Floor> getFloors() {
        return List.of();
    }


    private BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setSeatId(booking.getSeat().getId());
        response.setUserId(booking.getUser().getId());
        response.setBookingDate(booking.getBookingDate());
        response.setBookingTime(booking.getBookingTime());
        response.setCreatedAt(booking.getCreatedAt());
        response.setStatus(booking.getStatus().name());
        return response;
    }
}

