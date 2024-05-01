package com.example.jeebackend.Services;


import com.example.jeebackend.Entities.Event;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

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
            em.getTransaction().begin();
            em.persist(event);
            em.getTransaction().commit();
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
                existingEvent.setType(updatedEvent.getType());

                em.merge(existingEvent);
            }
            em.merge(updatedEvent);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }



    @Transactional
    public void deleteEventById(Long id) {
        try {
            em.getTransaction().begin();
            Event event = em.find(Event.class, id);
            if (event != null) {
                em.remove(event);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }



}
