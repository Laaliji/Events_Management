package com.example.metier.services;


import com.example.metier.entities.User;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import com.example.metier.entities.Event;
import java.util.List;
@Stateless
@Local
public class EventService {
    EntityManagerFactory emf;
    EntityManager em;



    public EventService() {
        emf = Persistence.createEntityManagerFactory("jee");
        em = emf.createEntityManager();
    }
    public void createEvent(Event event) {
        try {
            em.persist(event);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Event findEventById(Long id) {
        try {
            return em.find(Event.class, id);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public List<Event> findAllEvents() {
        try {
            return em.createQuery("SELECT e FROM Event e", Event.class).getResultList();
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public void updateEvent(Long eventid ,Event updatedEvent) {
        try {

            Event existingEvent = findEventById(eventid);
            if (existingEvent != null) {
                existingEvent.setTitle(updatedEvent.getTitle());
                existingEvent.setDescription(updatedEvent.getDescription());
                existingEvent.setDateTime(updatedEvent.getDateTime());
                existingEvent.setType(updatedEvent.getType());

                em.merge(existingEvent);
            }
            em.merge(updatedEvent);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void deleteEventById(Long id) {
        try {
            Event event = em.find(Event.class, id);
            if (event != null) {
                em.remove(event);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }



}
