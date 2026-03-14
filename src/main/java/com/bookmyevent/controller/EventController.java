package com.bookmyevent.controller;

import com.bookmyevent.dto.request.EventRequest;
import com.bookmyevent.dto.response.ApiResponse;
import com.bookmyevent.dto.response.EventResponse;
import com.bookmyevent.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    private final EventService eventService;

    // ===== PUBLIC ENDPOINTS (no login needed) =====

    @GetMapping("/events/public/all")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getAllEvents() {
        List<EventResponse> events = eventService.getAllPublishedEvents();
        return ResponseEntity.ok(ApiResponse.success("Events fetched!", events));
    }

    @GetMapping("/events/public/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(
            @PathVariable Long id) {
        EventResponse event = eventService.getEventById(id);
        return ResponseEntity.ok(ApiResponse.success("Event fetched!", event));
    }

    @GetMapping("/events/public/city/{city}")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByCity(
            @PathVariable String city) {
        List<EventResponse> events = eventService.getEventsByCity(city);
        return ResponseEntity.ok(ApiResponse.success("Events fetched!", events));
    }

    // ===== ORGANIZER ENDPOINTS (ROLE_ORGANIZER only) =====

    @PostMapping("/events/organizer/create")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Valid @RequestBody EventRequest request) {
        EventResponse event = eventService.createEvent(request);
        return ResponseEntity.ok(ApiResponse.success("Event created!", event));
    }

    @PutMapping("/events/organizer/update/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        EventResponse event = eventService.updateEvent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Event updated!", event));
    }

    @PutMapping("/events/organizer/publish/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> publishEvent(
            @PathVariable Long id) {
        EventResponse event = eventService.publishEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event published!", event));
    }

    @PutMapping("/events/organizer/cancel/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> cancelEvent(
            @PathVariable Long id) {
        EventResponse event = eventService.cancelEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event cancelled!", event));
    }

    @GetMapping("/events/organizer/my-events")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getMyEvents() {
        List<EventResponse> events = eventService.getMyEvents();
        return ResponseEntity.ok(ApiResponse.success("Your events fetched!", events));
    }
}