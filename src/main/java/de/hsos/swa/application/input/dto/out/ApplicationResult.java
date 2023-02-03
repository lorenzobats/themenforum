package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Topic;

public class ApplicationResult<T> {

    public enum Status {
        OK,                     // 200 (OK), 201 (CREATED)

        NO_CONTENT,             // 204 (NO Content) -> bei DELETE
        NOT_AUTHORIZED,         // 401 (Unauthorized)
        NO_ACCESS,              // 403 (Forbidden)
        NOT_FOUND,              // 404 (Not Found)
        NOT_VALID,              // 400 (Bad Request)
        NO_PERMISSION,          // 550 (Permission denied)
        EXCEPTION,              // 500 (Internal Server Error)
    }

    private final Status status;
    private T data;
    private String errorMessage = "";

    public ApplicationResult() {
        this.status = Status.EXCEPTION;
        this.data = null;
    }

    private ApplicationResult(T data) {
        this.status = Status.OK;
        this.data = data;
    }

    private ApplicationResult(Status status) {
        this.status = status;
        this.data = null;
    }

    private ApplicationResult(Status status, String errorMessage) {
        this.status = status;
        this.data = null;
        this.errorMessage = errorMessage;
    }

    // 200
    public static <T> ApplicationResult<T> ok(T data) {
        return new ApplicationResult<>(data);
    }
    public static <T> ApplicationResult<T> noContent(T data) {
        return new ApplicationResult<>(data);
    }

    // 400
    public static <T> ApplicationResult<T> noAuthorization() {
        return new ApplicationResult<>(Status.NOT_AUTHORIZED, "User cannot be authorized");
    }
    public static <T> ApplicationResult<T> noAuthorization(String errorMessage) {
        return new ApplicationResult<>(Status.NOT_AUTHORIZED, errorMessage);
    }
    public static <T> ApplicationResult<T> noAccess() {
        return new ApplicationResult<>(Status.NO_ACCESS, "Access cannot be granted");
    }
    public static <T> ApplicationResult<T> noAccess(String errorMessage) {
        return new ApplicationResult<>(Status.NO_ACCESS, errorMessage);
    }

    public static <T> ApplicationResult<T> notValid() {return new ApplicationResult<>(Status.NOT_VALID, "The request is invalid");}
    public static <T> ApplicationResult<T> notValid(String errorMessage) {return new ApplicationResult<>(Status.NOT_VALID, errorMessage);}
    public static <T> ApplicationResult<T> notFound() {
        return new ApplicationResult<>(Status.NOT_FOUND, "Requested ressource cannot be found");
    }
    public static <T> ApplicationResult<T> notFound(String errorMessage) {
        return new ApplicationResult<>(Status.NOT_FOUND, errorMessage);
    }


    // 500
    public static <T> ApplicationResult<T> exception() {
        return new ApplicationResult<T>(Status.EXCEPTION);
    }
    public static <T> ApplicationResult<T> exception(String errorMessage) {
        return new ApplicationResult<T>(Status.EXCEPTION, errorMessage);
    }
    public static <T> ApplicationResult<T> noPermission() {return new ApplicationResult<>(Status.NO_PERMISSION, "No permission to perform this operation");}
    public static <T> ApplicationResult<T> noPermission(String errorMessage) {return new ApplicationResult<>(Status.NO_PERMISSION, errorMessage);}

    public ApplicationResult<T> setData(T data){
        this.data = data;
        return this;
    }

    public ApplicationResult<T> setMessage(String errorMessage){
        this.errorMessage = errorMessage;
        return this;
    }

    public T data() {
        if (data != null) return data;
        else throw new RuntimeException("Error: No Data available");
    }

    public boolean ok() {
        return status.equals(Status.OK);
    }

    public String message() {
        return errorMessage;
    }


    public Status status(){
        return this.status;
    }

}
