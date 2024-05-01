
package com.example.jeebackend.rest;


import com.example.jeebackend.Entities.User;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import com.example.jeebackend.Services.AuthenticationService;
import jakarta.ws.rs.core.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.jeebackend.Services.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiReactUser {

    @EJB
    private UserService userService;

    @Context
    private HttpServletRequest httpServletRequest;

    @Inject
    private AuthenticationService authenticationService;


    @POST
    @Path("/authenticate")
    public Response authenticateUser(User credentials) {
        String token = userService.authenticateUser(credentials.getEmail(), credentials.getPassword());
        if (token != null) {
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/login")
    public Response loginUser(User loginUser) {
        User user = userService.login(loginUser.getEmail(), loginUser.getPassword());
        if (user != null) {
            String token = authenticationService.generateJwtToken(user);
            return Response.ok().entity(token).build();
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


    @GET
    public Response getAllUsers(@Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN")) {
                List<User> users = userService.findAllUsers();
                return Response.ok(users).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("/{id}")
    public Response getUserById(@PathParam("id") long id, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN" or "USER")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN") || role.equals("USER")) {
                User user = userService.findById(id);
                if (user != null) {
                    return Response.ok(user).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }




    @POST
    public Response createUser(User user) {
        try {
            if (userExists(user.getEmail())) {
                return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
            }
            String hashedPassword = PasswordHashing(user.getPassword());
            user.setPassword(hashedPassword);

            userService.register(user);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {

            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
        }
    }
    private boolean userExists(String email) {
        return userService.findByEmail(email) != null;
    }


    public String PasswordHashing(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        System.out.println("Hashed Password: " + hashedPassword); // Add this line
        return hashedPassword;
    }


    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") long id, User updatedUser, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN" or "USER")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN") || role.equals("USER")) {
                String hashedPassword = PasswordHashing(updatedUser.getPassword());
                updatedUser.setPassword(hashedPassword);
                userService.updateUser(id, updatedUser);
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") long id, @Context HttpServletRequest request) {
        String token = getJwtTokenFromRequest(request);
        if (token != null && authenticationService.validateToken(token)) {
            // Check if the user has the required role (e.g., "ADMIN")
            String role = authenticationService.getRoleFromToken(token);
            if (role.equals("ADMIN")) {
                userService.deleteUserByID(id);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
