package com.ascendion.demo.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseRequest {
    private Long seatId;
    private Long userId;
    private LocalDate bookingDate;
}

