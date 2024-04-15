package com.example.metier.API;

import com.example.metier.entities.Event;
import com.example.metier.entities.User;
import com.example.metier.services.EventService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/Event")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiReactEvent {

    @EJB
    private EventService EventService;

    @GET
    public List<Event> getAllUsers() {
        return EventService.findAllEvents();
    }
    @GET
    @Path("/{id}")
    public Response getEventById(@PathParam("id") long id) {
        Event event = EventService.findEventById(id);
        if (event != null) {
            return Response.ok(event).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    @POST
    public Response createEvent(Event event) {
        EventService.createEvent(event);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEvent(@PathParam("id") long id, Event updatedevent) {
        EventService.updateEvent(id, updatedevent);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") long id) {
        EventService.deleteEventById(id);
        return Response.noContent().build();
    }
}
}
