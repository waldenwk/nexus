package com.nexus.event.service;

import com.nexus.event.entity.Event;
import com.nexus.event.entity.EventAttendee;
import com.nexus.event.repository.EventAttendeeRepository;
import com.nexus.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;
    
    public Event createEvent(Event event) {
        event.setCreatedAt(java.time.LocalDateTime.now());
        event.setStatus(com.nexus.event.entity.EventStatus.ACTIVE);
        return eventRepository.save(event);
    }
    
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }
    
    public EventAttendee registerForEvent(Long eventId, EventAttendee attendee) {
        attendee.setEventId(eventId);
        attendee.setRegisteredAt(java.time.LocalDateTime.now());
        return eventAttendeeRepository.save(attendee);
    }
    
    public List<EventAttendee> getEventAttendees(Long eventId) {
        return eventAttendeeRepository.findByEventId(eventId);
    }
}