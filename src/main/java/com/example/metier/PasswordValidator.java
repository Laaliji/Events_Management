package com.example.metier;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        String password = (String) o;
        UIInput uiPassword2 = (UIInput) uiComponent.getAttributes().get("password2");
        String password2 = (String) uiPassword2.getSubmittedValue();

        if (password == null || password.isEmpty() || password2 == null || password2.isEmpty()) {
            return;
        }

        if (password.length() < 8) {
            uiPassword2.setValid(false);
            String messageText = "Password must contains at least 8 characters.";
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, messageText));
        }

        if (!password.equals(password2)) {
            uiPassword2.setValid(false);
            String messageText = "Passwords do not match.";
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageText, messageText));
        }
    }
}
