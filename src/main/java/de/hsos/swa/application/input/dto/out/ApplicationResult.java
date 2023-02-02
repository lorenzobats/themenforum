package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;

public class ApplicationResult<T> {

    public enum Status {
        OK,
        NOT_CREATED,
        NOT_UPDATED,
        NOT_FOUND,
        NOT_AUTHORIZED,
        EXCEPTION,
    }

    private final Status status;
    private final T data;
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

    public static <T> ApplicationResult<T> success(T data) {
        return new ApplicationResult<>(data);
    }

    public static <T> ApplicationResult<T> error(String errorMessage) {
        return new ApplicationResult<T>(Status.EXCEPTION, errorMessage);
    }

    public static <T> ApplicationResult<T> notFound() {
        return new ApplicationResult<>(Status.NOT_FOUND);
    }

    public T getData() {
        if (data != null) return data;
        else throw new RuntimeException("Error: No Data available");
    }

    public boolean isSuccessful() {
        return status.equals(Status.OK);
    }

    public String getMessage() {
        return errorMessage;
    }


    public boolean ok(){
        return status.equals(Status.OK);
    }

    public boolean badResult(){
        return !this.ok();
    }

    public Status status(){
        return this.status;
    }
    public T get(){
        return this.data;
    }


}
