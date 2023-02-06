package de.hsos.swa.application.output.repository.dto.in;

public class RepositoryResult<T> {
    public enum Status {
        OK,
        ENTITY_NOT_FOUND,
        EXCEPTION
    }

    private final Status status;
    private T data;


    public RepositoryResult() {
        this.status = Status.EXCEPTION;
        this.data = null;
    }

    private RepositoryResult(T data) {
        this.status = Status.OK;
        this.data = data;
    }
    private RepositoryResult(Status status) {
        this.status = status;
        this.data = null;
    }

    public static <T> RepositoryResult<T> ok(T data) {
        return new RepositoryResult<T>(data);
    }

    public static <T> RepositoryResult<T> exception() {
        return new RepositoryResult<T>(Status.EXCEPTION);
    }

    public static <T> RepositoryResult<T> notFound() {
        return new RepositoryResult<T>(Status.ENTITY_NOT_FOUND);
    }

    public RepositoryResult<T> setData(T data){
        this.data = data;
        return this;
    }

    public boolean ok(){
        return status.equals(Status.OK);
    }

    public boolean error(){
        return !this.ok();
    }

    public Status status(){
        return this.status;
    }
    public T get(){
        return this.data;
    }
}
