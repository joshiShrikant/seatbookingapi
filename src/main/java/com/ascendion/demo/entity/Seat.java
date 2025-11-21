package com.ascendion.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Access(AccessType.FIELD)
@Entity
@Data
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Floor floor;

    private String seatNumber;

    private Integer xCoordinate; // optional: x coordinate for seat location
    private Integer yCoordinate;  // optional: y coordinate for seat location

    private boolean active = true; // true if the seat is active/bookable, false if deactivated

    private boolean status; // true if occupied, false if available
}
