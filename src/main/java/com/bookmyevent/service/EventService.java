package com.bookmyevent.service;

import com.bookmyevent.dto.request.EventRequest;
import com.bookmyevent.dto.response.EventResponse;
import com.bookmyevent.entity.Event;
import com.bookmyevent.entity.User;
import com.bookmyevent.enums.EventStatus;
import com.bookmyevent.exception.CustomExceptions;
import com.bookmyevent.repository.EventRepository;
import com.bookmyevent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventResponse createEvent(EventRequest request) {
        User organizer = getCurrentUser();

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .venue(request.getVenue())
                .city(request.getCity())
                .eventDate(request.getEventDate())
                .bookingDeadline(request.getBookingDeadline())
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getTotalSeats())
                .ticketPrice(request.getTicketPrice())
                .status(EventStatus.DRAFT)
                .organizer(organizer)
                .build();

        eventRepository.save(event);
        return mapToResponse(event);
    }

    public EventResponse updateEvent(Long eventId, EventRequest request) {
        User organizer = getCurrentUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomExceptions.EventNotFoundException("Event not found!"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new CustomExceptions.UnauthorizedException("You are not authorized to update this event!");
        }

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setVenue(request.getVenue());
        event.setCity(request.getCity());
        event.setEventDate(request.getEventDate());
        event.setBookingDeadline(request.getBookingDeadline());
        event.setTicketPrice(request.getTicketPrice());

        eventRepository.save(event);
        return mapToResponse(event);
    }

    public EventResponse publishEvent(Long eventId) {
        User organizer = getCurrentUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomExceptions.EventNotFoundException("Event not found!"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new CustomExceptions.UnauthorizedException("You are not authorized to publish this event!");
        }

        event.setStatus(EventStatus.PUBLISHED);
        eventRepository.save(event);
        return mapToResponse(event);
    }

    public EventResponse cancelEvent(Long eventId) {
        User organizer = getCurrentUser();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomExceptions.EventNotFoundException("Event not found!"));

        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new CustomExceptions.UnauthorizedException("You are not authorized to cancel this event!");
        }

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
        return mapToResponse(event);
    }

    public List<EventResponse> getMyEvents() {
        User organizer = getCurrentUser();
        return eventRepository.findByOrganizerId(organizer.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EventResponse> getAllPublishedEvents() {
        return eventRepository.findByStatus(EventStatus.PUBLISHED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EventResponse> getEventsByCity(String city) {
        return eventRepository.findByCityAndStatus(city, EventStatus.PUBLISHED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomExceptions.EventNotFoundException("Event not found!"));
        return mapToResponse(event);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("User not found!"));
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .venue(event.getVenue())
                .city(event.getCity())
                .eventDate(event.getEventDate())
                .bookingDeadline(event.getBookingDeadline())
                .totalSeats(event.getTotalSeats())
                .availableSeats(event.getAvailableSeats())
                .ticketPrice(event.getTicketPrice())
                .status(event.getStatus())
                .organizerName(event.getOrganizer().getFullName())
                .createdAt(event.getCreatedAt())
                .build();
    }
}