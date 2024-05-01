package com.example.jeebackend.rest;

import com.example.jeebackend.Entities.Program;
import com.example.jeebackend.Services.AuthenticationService;
import com.example.jeebackend.Services.ProgramService;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/programs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiReactProgram {

    @EJB
    private ProgramService programService;

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
    public Response getAllPrograms(@Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ORGANIZER")) {
            List<Program> programs = programService.findAllPrograms();
            return Response.ok(programs).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProgramById(@PathParam("id") long id, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ORGANIZER")) {
            Program program = programService.findProgramById(id);
            if (program != null) {
                return Response.ok(program).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    public Response createProgram(Program program, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ORGANIZER")) {
            try {
                programService.createProgram(program);
                return Response.status(Response.Status.CREATED).build();
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProgram(@PathParam("id") long id, Program updatedProgram, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ORGANIZER")) {
            Program existingProgram = programService.findProgramById(id);
            if (existingProgram == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Program not found").build();
            }
            programService.updateProgram(id, updatedProgram);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProgram(@PathParam("id") long id, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (authenticationService.isAuthorized(token, "ORGANIZER")) {
            Program programToRemove = programService.findProgramById(id);
            if (programToRemove == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Program not found").build();
            }
            programService.deleteProgramByID(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}

