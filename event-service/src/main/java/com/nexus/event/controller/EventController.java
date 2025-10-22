package com.nexus.event.controller;

import com.nexus.event.entity.Event;
import com.nexus.event.entity.EventAttendee;
import com.nexus.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    
    @Autowired
    private EventService eventService;
    
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{eventId}/attendees")
    public EventAttendee registerForEvent(@PathVariable Long eventId, @RequestBody EventAttendee attendee) {
        return eventService.registerForEvent(eventId, attendee);
    }
    
    @GetMapping("/{eventId}/attendees")
    public List<EventAttendee> getEventAttendees(@PathVariable Long eventId) {
        return eventService.getEventAttendees(eventId);
    }
}