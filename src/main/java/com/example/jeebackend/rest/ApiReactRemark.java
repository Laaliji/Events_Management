package com.example.jeebackend.rest;

import com.example.jeebackend.Entities.Remark;
import com.example.jeebackend.Entities.User;
import com.example.jeebackend.Services.AuthenticationService;
import com.example.jeebackend.Services.RemarkService;
import com.example.jeebackend.Services.UserService;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



import java.time.LocalDateTime;
import java.util.List;

@Path("/remarks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiReactRemark {

    @EJB
    private RemarkService remarkService;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private UserService userService;

    @Context
    private HttpServletRequest httpServletRequest;



    @POST
    @Path("/create")
    public Response createRemark(Remark remark) {
        String token = getJwtTokenFromRequest(httpServletRequest);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN")) {
                String userEmail = authenticationService.getEmailFromToken(token);
                User user = userService.findByEmail(userEmail);

                // Create a new Remark instance with the provided fields
                Remark newRemark = new Remark();
                newRemark.setDescription(remark.getDescription());
                newRemark.setPriority(remark.getPriority());
                newRemark.setStatus(remark.getStatus());
                newRemark.setType(remark.getType());
                newRemark.setEvent(remark.getEvent()); // Set the user directly


                remarkService.registerRemark(user, newRemark);
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }


    @GET
    public Response getAllRemarks() {
        String token = getJwtTokenFromRequest(httpServletRequest);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN")) {
                List<Remark> remarks = remarkService.getAllRemarks();
                return Response.ok(remarks).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("/{id}")
    @GET
    public Response getRemarkById(@PathParam("id") long id) {
        String token = getJwtTokenFromRequest(httpServletRequest);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN")) {
                Remark remark = remarkService.getRemarkById(id);
                if (remark != null) {
                    return Response.ok(remark).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("Remark not found").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("/{id}")
    @PUT
    public Response updateRemark(@PathParam("id") long id, Remark updatedRemark) {
        String token = getJwtTokenFromRequest(httpServletRequest);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN")) {
                remarkService.updateRemark(id, updatedRemark, token);
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }



    @Path("/{id}")
    @DELETE
    public Response deleteRemark(@PathParam("id") long id) {
        String token = getJwtTokenFromRequest(httpServletRequest);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN")) {
                remarkService.deleteRemark(id);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
