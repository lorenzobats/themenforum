package de.hsos.swa.domain.validation.validator;

import de.hsos.swa.domain.validation.constraints.ValidId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class IdValidator implements ConstraintValidator<ValidId, String> {

    private String message;
    private String detail;

    @Override
    public void initialize(ValidId constraintAnnotation) {
        message = constraintAnnotation.message();
        detail = constraintAnnotation.detail();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("detail")
                    .addConstraintViolation();
            return false;
        }
    }
}
