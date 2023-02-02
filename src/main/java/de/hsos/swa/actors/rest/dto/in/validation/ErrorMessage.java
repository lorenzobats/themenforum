package de.hsos.swa.actors.rest.dto.in.validation;

public class ErrorMessage {
    public String message;
    public String detail;

    public ErrorMessage(String message, String detail) {
        this.message = message;
        this.detail = detail;
    }
}
