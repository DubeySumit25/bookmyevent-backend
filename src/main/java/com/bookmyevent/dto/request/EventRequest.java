package com.bookmyevent.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;

    @NotNull(message = "Booking deadline is required")
    private LocalDateTime bookingDeadline;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "At least 1 seat required")
    private Integer totalSeats;

    @NotNull(message = "Ticket price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal ticketPrice;
}