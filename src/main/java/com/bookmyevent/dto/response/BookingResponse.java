package com.bookmyevent.dto.response;

import com.bookmyevent.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {

    private Long id;
    private String bookingReference;
    private String eventTitle;
    private String eventVenue;
    private LocalDateTime eventDate;
    private Integer numberOfTickets;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private List<String> ticketNumbers;
    private LocalDateTime createdAt;
}