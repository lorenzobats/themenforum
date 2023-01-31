package de.hsos.swa.actors.ui.validation;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// https://www.baeldung.com/rest-api-error-handling-best-practices
public class UIValidationResult {

    public String timestamp;
    public Response.Status status;

    public String path;
    public List<ValidationResultError> errors = new ArrayList<>();

    public UIValidationResult(String timestamp, Response.Status status, String path, List<ValidationResultError> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.path = path;
        this.errors = errors;
    }

    public UIValidationResult(Set<? extends ConstraintViolation<?>> violations, Response.Status status, String path) {
        this.status = status;
        this.path = path;
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
        public ValidationResultError() {}
    }
}
