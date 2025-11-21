package com.ascendion.demo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long seatId;
    private Long userId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
}

