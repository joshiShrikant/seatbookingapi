package com.ascendion.demo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;
    private Long seatId;
    private Long userId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private LocalDateTime createdAt;
    private String status;
}

