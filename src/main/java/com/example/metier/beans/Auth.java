package com.example.metier.beans;

import com.example.metier.entities.User;
import com.example.metier.entities.Role;
import com.example.metier.services.UserService;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;

// annotated with sessionScoped to ensure that
// the user infos are available string the whole session

@ManagedBean
@SessionScoped
public class Auth implements Serializable {
    // injecting the service
    @EJB
    private UserService userService;
    // for storing the logged user
    private User loggedUser;

    // used in registering and login
    private User userReg;
    private String email;
    private String password;

    public Auth() {
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getUserReg() {
        return userReg;
    }

    public void setUserReg(User userReg) {
        this.userReg = userReg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init(){
        userReg = new User();
    }

    // the registering method used in the jsf
    public String register()
    {

        if (userService.findByEmail(email) != null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","The provided email is already used."));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "register";
        }
        userService.register(userReg);
        this.loggedUser = userReg;
        HttpSession session = SessionUtils.getSession();
        session.setAttribute("user", userReg);
        return "home";
    }

    public String login()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        User userCheck = userService.login(email, password);
        if(userCheck != null)
        {
            this.loggedUser = userCheck;
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("user", userCheck);
            return "home";
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "Wrong email or password"));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "login";
        }
    }

    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        this.loggedUser = null;
        return "login";
    }

    // method to verify is the logged user is an admin
    public boolean isAdmin(){
        return this.loggedUser != null && this.loggedUser.getRole().equals(Role.ADMIN);
    }

}

