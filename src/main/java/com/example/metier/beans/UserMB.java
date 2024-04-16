package com.example.metier.beans;

import com.example.metier.entities.User;
import com.example.metier.services.UserService;
import jakarta.annotation.ManagedBean;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class UserMB implements Serializable {
    @EJB
    private UserService userService;

    public UserMB() {
    }

    public boolean isExistsUserWithEmail(String email) {
        User user = userService.findByEmail(email);
        return user != null;
    }
}
