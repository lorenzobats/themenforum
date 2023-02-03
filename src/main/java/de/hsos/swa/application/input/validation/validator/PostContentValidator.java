package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPostContent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostContentValidator implements ConstraintValidator<ValidPostContent, String> {
    private String message;

    @Override
    public void initialize(ValidPostContent annotation) {
    }

    @Override
    public boolean isValid(String content, ConstraintValidatorContext context) {
        if (content == null)
            return true;

        if (content.length() < 2 || content.length() > 250) {
            return constraintValidation(context, "Post content must be between 2 and 250 characters");
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
