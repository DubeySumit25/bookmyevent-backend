package com.bookmyevent.dto.response;

import com.bookmyevent.enums.EventStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private String venue;
    private String city;
    private LocalDateTime eventDate;
    private LocalDateTime bookingDeadline;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal ticketPrice;
    private EventStatus status;
    private String organizerName;
    private LocalDateTime createdAt;
}