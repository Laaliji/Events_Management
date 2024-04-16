package com.example.metier.beans;

import com.example.metier.entities.User;
import com.example.metier.services.UserService;
import com.example.metier.services.EventService;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import com.example.metier.entities.Event;
import jakarta.inject.Named;


import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class EventMB implements Serializable {
    @EJB
    private EventService eventService;
    private Event event;

    @PostConstruct
    public void init(){
        event = new Event();
    }
    public EventMB() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String save() {
        if (eventService.saveEvent(event) != null){
            return "admin";
        }
        return "admin?error";
    }

    public List<Event> allEvents(){
        return eventService.getAll();
    }

    public String delete(Long id){
        eventService.deleteEvent(id);
        return "admin";
    }
}
