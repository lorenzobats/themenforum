package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidCommentText;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommentTextValidator implements ConstraintValidator<ValidCommentText, String> {
    private String message;

    @Override
    public void initialize(ValidCommentText constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null)
            return constraintValidation(context, "Provided comment text is null");

        if (value.isEmpty())
            return constraintValidation(context, "Ensure that the provided comment text is not empty");

        int length = value.length();
        if(length < 2)
            return constraintValidation(context, "Ensure that the provided comment text has a minimum length of 2 characters");
        if(length > 251)
            return constraintValidation(context, "Ensure that the provided comment text has a maximum length of 250 characters");
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