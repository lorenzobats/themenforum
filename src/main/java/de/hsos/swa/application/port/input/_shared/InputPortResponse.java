package de.hsos.swa.application.port.input._shared;

public class InputPortResponse <T> {

    private InputPortResponseStatus status;
    private String errorMessage;

    public InputPortResponse(InputPortResponseStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public InputPortResponseStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
