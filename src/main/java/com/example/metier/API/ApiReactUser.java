
package com.example.metier.API;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import com.example.metier.entities.User;
import com.example.metier.services.UserService;

@ApplicationPath("/api")
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiReactUser {

    @EJB
    private UserService userService;

    @GET
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }



    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") long id) {
        User user = userService.findById(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createUser(User user) {
        userService.register(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") long id, User updatedUser) {
        userService.updateUser(id, updatedUser);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") long id) {
        userService.deleteUserByID(id);
        return Response.noContent().build();
    }
}
