package de.hsos.swa.application.output.repository.dto.out;

public class RepositoryResult<T> {
    public enum Status {
        OK,
        ENTITY_NOT_FOUND,
        ENTITY_NOT_PERSISTED,
        ERROR
    }

    private final Status status;
    private final T data;


    public RepositoryResult() {
        this.status = Status.ERROR;
        this.data = null;
    }

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
