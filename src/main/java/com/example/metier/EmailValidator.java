package com.example.metier;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String email=o.toString();
        if (email==null || email.isEmpty())
            return;
        if (!email.matches("[a-zA-Z][A-Za-z0-9._]*@[a-z]+([.][a-z]*)+")) {
            String messageText = "Please enter a valid email.";
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"",messageText));
        }
    }
}
