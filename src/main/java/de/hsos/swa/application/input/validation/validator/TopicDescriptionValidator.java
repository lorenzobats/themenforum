package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidTopicDescription;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TopicDescriptionValidator implements ConstraintValidator<ValidTopicDescription, String> {
    private String message;

    @Override
    public void initialize(ValidTopicDescription annotation) {
        message = annotation.message();
    }

    @Override
    public boolean isValid(String content, ConstraintValidatorContext context) {
        if (content == null) {
            return constraintValidation(context, "No Topic Description provided");
        }

        if (content.isEmpty())
            return constraintValidation(context, "Topic Description is empty");


        if (content.length() < 2 || content.length() > 100) {
            return constraintValidation(context, "Topic Description must be between 2 and 100 characters");
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
