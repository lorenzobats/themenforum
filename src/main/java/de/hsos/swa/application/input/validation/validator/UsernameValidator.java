package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidUsername;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private String message;

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null)
            return constraintValidation(context);

        if (value.isEmpty())
            return constraintValidation(context);

        if (value.matches(".*\\s+.*")) {
            return constraintValidation(context, "Ensure that the username contains no spaces");
        }
        if (!value.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
            return constraintValidation(context, "Ensure that the username contains only lowercase letters and numbers and starts with a letter");
        }
        int length = value.length();
        if(length < 4)
            return constraintValidation(context, "Ensure that the username has a minimum length of 4 characters");
        if(length > 20)
            return constraintValidation(context, "Ensure that the username has a maximum length of 20 characters");
        return true;
    }

    private boolean constraintValidation(ConstraintValidatorContext context) {
        return constraintValidation(context, message);
    }

    private boolean constraintValidation(ConstraintValidatorContext context, String defaultMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(defaultMessage)
                .addConstraintViolation();
        return false;
    }
}