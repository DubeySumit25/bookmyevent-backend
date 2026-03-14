package com.bookmyevent.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Number of tickets is required")
    @Min(value = 1, message = "At least 1 ticket required")
    @Max(value = 10, message = "Cannot book more than 10 tickets at once")
    private Integer numberOfTickets;
}