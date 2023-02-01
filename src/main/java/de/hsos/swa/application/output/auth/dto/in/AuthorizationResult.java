package de.hsos.swa.application.output.auth.dto.in;

public class AuthorizationResult<T> {
    public enum Status {
        OK,
        USER_NOT_AUTHENTICATED,
        ACCESS_DENIED,
        ERROR
    }

    public final Status status;
    public final T data;


    private AuthorizationResult(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <Void> AuthorizationResult<Void> ok() {
        return new AuthorizationResult<>(Status.OK, null);
    }
    public static <T> AuthorizationResult<T> ok(T data) {
        return new AuthorizationResult<T>(Status.OK, data);
    }

    public static <T> AuthorizationResult<T> error() {
        return new AuthorizationResult<T>(Status.ERROR, (T) null);
    }

    public static <T> AuthorizationResult<T> notAuthenticated() {
        return new AuthorizationResult<T>(Status.USER_NOT_AUTHENTICATED, (T) null);
    }

    public static <T> AuthorizationResult<T> notAllowed() {
        return new AuthorizationResult<T>(Status.ACCESS_DENIED, (T) null);
    }

    public boolean valid(){
        return status.equals(Status.OK);
    }

    public boolean invalid(){
        return !this.valid();
    }

    public Status status(){
        return this.status;
    }
    public T get(){
        return this.data;
    }
}