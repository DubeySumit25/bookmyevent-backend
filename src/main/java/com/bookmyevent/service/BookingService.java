package com.bookmyevent.service;

import com.bookmyevent.dto.request.BookingRequest;
import com.bookmyevent.dto.response.BookingResponse;
import com.bookmyevent.entity.Booking;
import com.bookmyevent.entity.Event;
import com.bookmyevent.entity.Ticket;
import com.bookmyevent.entity.User;
import com.bookmyevent.enums.BookingStatus;
import com.bookmyevent.enums.EventStatus;
import com.bookmyevent.repository.BookingRepository;
import com.bookmyevent.repository.EventRepository;
import com.bookmyevent.repository.TicketRepository;
import com.bookmyevent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public BookingResponse bookTickets(BookingRequest request) {
        User buyer = getCurrentUser();

        // 1. Get event
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found!"));

        // 2. Check event is published
        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new RuntimeException("Event is not available for booking!");
        }

        // 3. Check booking deadline
        if (LocalDateTime.now().isAfter(event.getBookingDeadline())) {
            throw new RuntimeException("Booking deadline has passed!");
        }

        // 4. Check available seats
        if (event.getAvailableSeats() < request.getNumberOfTickets()) {
            throw new RuntimeException("Not enough seats available!");
        }

        // 5. Check if user already booked this event
        boolean alreadyBooked = bookingRepository
                .existsByUserIdAndEventIdAndStatusNot(
                        buyer.getId(),
                        event.getId(),
                        BookingStatus.CANCELLED
                );
        if (alreadyBooked) {
            throw new RuntimeException("You have already booked this event!");
        }

        // 6. Calculate total amount
        BigDecimal totalAmount = event.getTicketPrice()
                .multiply(BigDecimal.valueOf(request.getNumberOfTickets()));

        // 7. Create booking
        Booking booking = Booking.builder()
                .bookingReference(generateBookingReference())
                .user(buyer)
                .event(event)
                .numberOfTickets(request.getNumberOfTickets())
                .totalAmount(totalAmount)
                .status(BookingStatus.CONFIRMED)
                .build();

        bookingRepository.save(booking);

        // 8. Generate individual tickets
        List<Ticket> tickets = new ArrayList<>();
        List<String> ticketNumbers = new ArrayList<>();

        for (int i = 0; i < request.getNumberOfTickets(); i++) {
            String ticketNumber = generateTicketNumber();
            Ticket ticket = Ticket.builder()
                    .ticketNumber(ticketNumber)
                    .booking(booking)
                    .seatNumber("SEAT-" + (event.getTotalSeats()
                            - event.getAvailableSeats() + i + 1))
                    .checkedIn(false)
                    .build();
            tickets.add(ticket);
            ticketNumbers.add(ticketNumber);
        }

        ticketRepository.saveAll(tickets);
        emailService.sendBookingConfirmation(booking);

        // 9. Reduce available seats
        event.setAvailableSeats(
                event.getAvailableSeats() - request.getNumberOfTickets()
        );
        eventRepository.save(event);

        return mapToResponse(booking, ticketNumbers);
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        User buyer = getCurrentUser();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found!"));

        // Only the buyer who booked can cancel
        if (!booking.getUser().getId().equals(buyer.getId())) {
            throw new RuntimeException("You are not authorized to cancel this booking!");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled!");
        }

        // Cancel booking
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        emailService.sendCancellationEmail(booking);

        // Restore available seats
        Event event = booking.getEvent();
        event.setAvailableSeats(
                event.getAvailableSeats() + booking.getNumberOfTickets()
        );
        eventRepository.save(event);

        List<String> ticketNumbers = ticketRepository
                .findByBookingId(booking.getId())
                .stream()
                .map(Ticket::getTicketNumber)
                .toList();

        return mapToResponse(booking, ticketNumbers);
    }

    public List<BookingResponse> getMyBookings() {
        User buyer = getCurrentUser();
        return bookingRepository.findByUserId(buyer.getId())
                .stream()
                .map(booking -> {
                    List<String> ticketNumbers = ticketRepository
                            .findByBookingId(booking.getId())
                            .stream()
                            .map(Ticket::getTicketNumber)
                            .toList();
                    return mapToResponse(booking, ticketNumbers);
                })
                .toList();
    }

    public BookingResponse getBookingByReference(String reference) {
        Booking booking = bookingRepository
                .findByBookingReference(reference)
                .orElseThrow(() -> new RuntimeException("Booking not found!"));

        List<String> ticketNumbers = ticketRepository
                .findByBookingId(booking.getId())
                .stream()
                .map(Ticket::getTicketNumber)
                .toList();

        return mapToResponse(booking, ticketNumbers);
    }

    // ===== HELPER METHODS =====

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    private String generateBookingReference() {
        return "BME-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateTicketNumber() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private BookingResponse mapToResponse(Booking booking, List<String> ticketNumbers) {
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .eventTitle(booking.getEvent().getTitle())
                .eventVenue(booking.getEvent().getVenue())
                .eventDate(booking.getEvent().getEventDate())
                .numberOfTickets(booking.getNumberOfTickets())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .ticketNumbers(ticketNumbers)
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
