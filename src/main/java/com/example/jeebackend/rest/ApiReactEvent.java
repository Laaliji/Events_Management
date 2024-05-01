package com.example.jeebackend.rest;

import com.example.jeebackend.Entities.Event;
import com.example.jeebackend.Services.AuthenticationService;
import com.example.jeebackend.Services.EventService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiReactEvent {

    @EJB
    private EventService eventService;

    @Inject
    private AuthenticationService authenticationService;

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    @GET
    @RolesAllowed({"ADMIN","USER"})
    public Response getAllEvents(@Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ADMIN") ||
                authenticationService.isAuthorized(token, "USER")) {
            List<Event> events = eventService.findAllEvents();
            return Response.ok(events).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    @GET
    @Path("/{id}")
    public Response getEventById(@PathParam("id") long id, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ADMIN")) {
            Event event = eventService.findEventById(id);
            if (event != null) {
                return Response.ok(event).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    public Response createEvent(Event event, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ADMIN")) {
            try {
                eventService.createEvent(event);
                return Response.status(Response.Status.CREATED).build();
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create event").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateEvent(@PathParam("id") long id, Event updatedEvent, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ADMIN")) {
            try {
                eventService.updateEvent(id, updatedEvent);
                return Response.noContent().build();
            } catch (Exception e) {
                return Response.serverError().entity("Failed to update event: " + e.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") long id, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ADMIN")) {
            Event eventToRemove = eventService.findEventById(id);
            if (eventToRemove == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Event not found").build();
            }
            eventService.deleteEventById(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
