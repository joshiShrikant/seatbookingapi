package com.ascendion.demo.dto;

import java.time.Instant;

public class SeatDto {
    public Long id;
    public String code;
    public String status; // e.g. "available", "booked", "unavailable"
    public Long bookedByUserId;
    public Instant bookedAt;

    public SeatDto() { }

    public SeatDto(Long id, String code, String status) {
        this.id = id;
        this.code = code;
        this.status = status;
    }
}
