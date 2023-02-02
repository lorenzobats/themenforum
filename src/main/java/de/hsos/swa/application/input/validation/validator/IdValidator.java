package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class IdValidator implements ConstraintValidator<ValidId, String> {
    private String message;

    @Override
    public void initialize(ValidId constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return UUID.fromString(value).toString().equals(value);
        } catch (IllegalArgumentException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
    }
}
