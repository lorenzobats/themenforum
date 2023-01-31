package de.hsos.swa.actors.rest.dto.in.validation;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// https://www.baeldung.com/rest-api-error-handling-best-practices
public class ValidationResult {

    public String timestamp;
    public Integer status;
    public String path;
    public List<ValidationResultError> errors = new ArrayList<>();


    // TODO: Refactor fuer Response
    public ValidationResult(String message) {
        this.status = 400;
        this.path = "/path/to/ressource";
        this.timestamp = LocalDateTime.now().toString();
        this.errors.add(new ValidationResultError(message));
    }

    public ValidationResult(Set<? extends ConstraintViolation<?>> violations) {
        this.status = 400;
        this.path = "/path/to/ressource";
        this.timestamp = LocalDateTime.now().toString();

        violations.forEach(violation -> {
            ValidationResultError error = new ValidationResultError();
            error.message = violation.getMessage();
            Object detail = violation.getConstraintDescriptor().getAttributes().get("detail");
            if (detail instanceof String) {
                error.detail = String.valueOf(detail);
            }
            this.errors.add(error);
        });
    }
    public class ValidationResultError {
        public String message;
        public  String detail;
        public ValidationResultError(String message) {
            this.message = message;
        }

        public ValidationResultError() {
        }
    }
}
