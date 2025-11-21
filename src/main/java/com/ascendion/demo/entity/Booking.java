package com.ascendion.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Access(AccessType.FIELD)
@Accessors(fluent = false)
@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    // explicitly map FK column on the field
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "seat_id", referencedColumnName = "id", nullable = false)
//    private Seat seat;


    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    private Seat seat;



    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private LocalDate bookingDate;

    private LocalTime bookingTime;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.BOOKED;

    public enum Status { BOOKED, CANCELLED }
}
