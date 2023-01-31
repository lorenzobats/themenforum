package de.hsos.swa.application.output.repository;

public class RepositoryResult<T> {
    public enum Status {
        OK,
        ENTITY_NOT_FOUND,
        ENTITY_NOT_PERSISTED,
        ERROR
    }

    public final Status status;
    public final T data;


    private RepositoryResult(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> RepositoryResult<T> ok(T data) {
        return new RepositoryResult<T>(Status.OK, data);
    }

    public static <T> RepositoryResult<T> error() {
        return new RepositoryResult<T>(Status.ERROR, (T) null);
    }

    public static <T> RepositoryResult<T> notFound() {
        return new RepositoryResult<T>(Status.ENTITY_NOT_FOUND, (T) null);
    }

    public static <T> RepositoryResult<T> notPersisted() {
        return new RepositoryResult<T>(Status.ENTITY_NOT_PERSISTED, (T) null);
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
