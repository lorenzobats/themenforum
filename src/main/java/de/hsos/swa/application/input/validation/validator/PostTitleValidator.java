package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPostTitle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostTitleValidator implements ConstraintValidator<ValidPostTitle, String> {
    private String message;

    @Override
    public void initialize(ValidPostTitle annotation) {
        message = annotation.message();
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null)
            return true;

        if(title.length() < 2 || title.length() > 40) {
            return constraintValidation(context, "Post title must be between 2 and 40 characters");
        }

        return true;
    }

    private boolean constraintValidation(ConstraintValidatorContext context, String defaultMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(defaultMessage)
                .addConstraintViolation();
        return false;
    }
}
