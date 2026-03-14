package com.bookmyevent.controller;

import com.bookmyevent.dto.request.BookingRequest;
import com.bookmyevent.dto.response.ApiResponse;
import com.bookmyevent.dto.response.BookingResponse;
import com.bookmyevent.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<BookingResponse>> bookTickets(
            @Valid @RequestBody BookingRequest request) {
        BookingResponse booking = bookingService.bookTickets(request);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed!", booking));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long id) {
        BookingResponse booking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled!", booking));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        List<BookingResponse> bookings = bookingService.getMyBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings fetched!", bookings));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ApiResponse<BookingResponse>> getByReference(
            @PathVariable String reference) {
        BookingResponse booking = bookingService.getBookingByReference(reference);
        return ResponseEntity.ok(ApiResponse.success("Booking fetched!", booking));
    }
}
