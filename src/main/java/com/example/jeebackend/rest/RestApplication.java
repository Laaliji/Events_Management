package com.example.jeebackend.rest;

import com.example.jeebackend.Entities.*;
import com.example.jeebackend.Services.*;
import com.example.jeebackend.config.JacksonConfig;
import jakarta.enterprise.event.Event;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JacksonConfig.class);
        classes.add(Program.class);
        classes.add(ProgramService.class);
        classes.add(ApiReactProgram.class);
        classes.add(User.class);
        classes.add(ApiReactUser.class);
        classes.add(UserService.class);
        classes.add(Event.class);
        classes.add(EventService.class);
        classes.add(ApiReactEvent.class);
        classes.add(EventType.class);
        classes.add(Role.class);
        classes.add(AuthenticationService.class);
        classes.add(RemarkType.class);
        classes.add(Remark.class);
        classes.add(ApiReactRemark.class);
        classes.add(RemarkService.class);
        return classes;
    }


    @OPTIONS
    public Response handleOptions() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "http://localhost:5173")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .build();
    }
}