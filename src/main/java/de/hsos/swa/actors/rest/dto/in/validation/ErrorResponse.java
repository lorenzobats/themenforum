package de.hsos.swa.actors.rest.dto.in.validation;

import de.hsos.swa.application.input.dto.out.ApplicationResult;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// TODO: QUELLE https://www.baeldung.com/rest-api-error-handling-best-practices
public class ErrorResponse {

    public String timestamp;
    public Integer status;
    public List<ErrorMessage> errors = new ArrayList<>();

    private ErrorResponse(String message, String detail, Integer status) {
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
        this.errors.add(new ErrorMessage(message, detail));
    }

    private ErrorResponse(Set<? extends ConstraintViolation<?>> violations) {
        this.status = 400;
        this.timestamp = LocalDateTime.now().toString();
        violations.forEach(violation -> this.errors.add(
                new ErrorMessage("Invalid request", violation.getMessage())
        ));
    }

    public static Response asResponseFromApplicationResult(ApplicationResult.Status status, String detailMessage) {
        switch (status) {
            case NOT_VALID -> {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Invalid request", detailMessage, 400)).build();
            }
            case NOT_AUTHORIZED -> {
                return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Valid Authorization required", detailMessage, 401)).build();
            }
            case NO_ACCESS -> {
                return Response.status(Response.Status.FORBIDDEN).entity(new ErrorResponse("Access to ressource forbidden", detailMessage, 403)).build();
            }
            case NOT_FOUND -> {
                return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse("Requested ressource could not be found", detailMessage, 404)).build();
            }
            case EXCEPTION -> {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Internal server error", detailMessage, 500)).build();
            }
            case NO_PERMISSION -> {
                return Response.status(Response.Status.FORBIDDEN).entity(new ErrorResponse("Operation on ressource not permitted", detailMessage, 550)).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Invalid request", detailMessage, 400)).build();
    }

    public static Response asResponseFromConstraintViolation(Set<ConstraintViolation<?>> constraintViolations) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(constraintViolations)).build();
    }
}


