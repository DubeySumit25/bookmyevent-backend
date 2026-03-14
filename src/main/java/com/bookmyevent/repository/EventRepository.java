package com.bookmyevent.repository;

import com.bookmyevent.entity.Event;
import com.bookmyevent.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(EventStatus status);

    List<Event> findByOrganizerIdAndStatus(Long organizerId, EventStatus status);

    List<Event> findByOrganizerId(Long organizerId);

    List<Event> findByCityAndStatus(String city, EventStatus status);
}