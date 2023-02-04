package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPostTitle;
import de.hsos.swa.application.input.validation.constraints.ValidTopicTitle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TopicTitleValidator implements ConstraintValidator<ValidTopicTitle, String> {
    private String message;

    @Override
    public void initialize(ValidTopicTitle annotation) {
        message = annotation.message();
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null) {
            return constraintValidation(context, "No Topic Title provided");
        }

        if (title.isEmpty())
            return constraintValidation(context, "Topic Title is empty");

        if (title.length() < 2 || title.length() > 40) {
            return constraintValidation(context, "Topic Title must be between 2 and 40 characters");
        }
        if (!title.matches("^[a-zA-Z0-9]+$")) {
            return constraintValidation(context, "Ensure that the Topic Title contains only letters and numbers");
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
