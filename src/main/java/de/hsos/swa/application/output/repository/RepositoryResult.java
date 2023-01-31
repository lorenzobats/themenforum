package de.hsos.swa.application.output.repository;

public class RepositoryResult<T> {
    public enum RepositoryStatus {
        OK,
        ENTITY_NOT_FOUND,
        ENTITY_NOT_PERSISTED,
        ERROR
    }

    public final RepositoryStatus status;
    public final T data;


    private RepositoryResult(RepositoryStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> RepositoryResult<T> ok(T data) {
        return new RepositoryResult<T>(RepositoryStatus.OK, data);
    }

    public static <T> RepositoryResult<T> error() {
        return new RepositoryResult<T>(RepositoryStatus.ERROR, (T) null);
    }

    public static <T> RepositoryResult<T> notFound() {
        return new RepositoryResult<T>(RepositoryStatus.ENTITY_NOT_FOUND, (T) null);
    }

    public static <T> RepositoryResult<T> notPersisted() {
        return new RepositoryResult<T>(RepositoryStatus.ENTITY_NOT_PERSISTED, (T) null);
    }

    public boolean ok(){
        return status.equals(RepositoryStatus.OK);
    }

    public boolean badResult(){
        return !this.ok();
    }

    public RepositoryStatus status(){
        return this.status;
    }
    public T get(){
        return this.data;
    }
}
