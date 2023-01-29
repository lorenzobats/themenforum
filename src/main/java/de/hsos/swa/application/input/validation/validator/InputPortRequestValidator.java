package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.UUID;

public class InputPortRequestValidator implements ConstraintValidator<ValidInputPortRequest, Record> {
    private String message;
    private String detail;

    @Override
    public void initialize(ValidInputPortRequest constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.detail = constraintAnnotation.detail();
    }

    @Override
    public boolean isValid(Record record, ConstraintValidatorContext context) {
        boolean isValid = true;
            for (Field field : record.getClass().getDeclaredFields()) {
                Object value;
                try {
                    field.setAccessible(true);
                    value = field.get(record);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    isValid = false;
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Missing field: " + field.getName() + " on " + record.getClass().getSimpleName())
                            .addPropertyNode("detail")
                            .addConstraintViolation();
                }
        }
        return isValid;
    }
}
