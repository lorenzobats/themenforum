package de.hsos.swa.infrastructure.rest.validation;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationResult {

    // TODO: Refactor fuer Response
    public ValidationResult(String message) {
        this.success = false;
        this.message = message;
    }


    public ValidationResult(Set<? extends ConstraintViolation<?>> violations) {
        this.success = false;
        this.message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }

    private String message;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
