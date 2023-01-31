package de.hsos.swa.infrastructure.authentication;

public class AuthenticationResult {

    public final RepositoryStatus status;
    public enum RepositoryStatus {
        OK,
        USER_NOT_AUTHENTICATED,
        ERROR
    }

    private AuthenticationResult(RepositoryStatus status) {
        this.status = status;
    }

    public static AuthenticationResult create(RepositoryStatus status) {
        return new AuthenticationResult(status);
    }

    public boolean ok(){
        return status.equals(RepositoryStatus.OK);
    }

    public RepositoryStatus get(){
        return this.status;
    }
}
