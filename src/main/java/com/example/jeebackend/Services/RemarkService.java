package com.example.jeebackend.Services;

import com.example.jeebackend.Entities.Event;
import com.example.jeebackend.Entities.Remark;
import com.example.jeebackend.Entities.User;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
@Local
public class RemarkService {

    @PersistenceContext

    private EntityManager em;

    @Inject
    private EventService eventService;

    public void registerRemark(User user, Remark remark) {
        if (user.isAdmin()) {
            try {
                // Fetch the latest event ID from the EventService
                List<Event> events = eventService.findAllEvents();
                if (!events.isEmpty()) {
                    Event latestEvent = events.get(events.size() - 1);
                    Long eventId = latestEvent.getId();

                    // Fetch the event using the retrieved event ID
                    Event event = eventService.findEventById(eventId);

                    if (event != null) {

                        remark.setEvent(event);
                        em.persist(remark);
                        em.merge(event);
                    } else {
                        throw new IllegalArgumentException("Event not found with id: " + eventId);
                    }
                } else {
                    throw new IllegalArgumentException("No events found in the database.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Only users with the role ADMIN can create remarks.");
        }
    }



    public List<Remark> getAllRemarks() {
        try {
            return em.createQuery("SELECT r FROM Remark r", Remark.class)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Remark getRemarkById(Long remarkId) {
        try {
            return em.find(Remark.class, remarkId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private UserService userService;

    public void updateRemark(long id, Remark updatedRemark, String token) {
        try {
            Remark existingRemark = em.find(Remark.class, id);
            if (existingRemark != null) {
                // Get the user email from the token
                String userEmail = authenticationService.getEmailFromToken(token);
                User user = userService.findByEmail(userEmail);

                // Update the properties of the existing remark
                existingRemark.setDescription(updatedRemark.getDescription());
                existingRemark.setPriority(updatedRemark.getPriority());
                existingRemark.setStatus(updatedRemark.getStatus());
                existingRemark.setType(updatedRemark.getType());
                existingRemark.setEvent(updatedRemark.getEvent());

                em.merge(existingRemark);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteRemark(Long remarkId){
        try {
            Remark remarkToDelete = em.find(Remark.class, remarkId);
            if (remarkToDelete != null) {
                em.remove(remarkToDelete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
