package de.hsos.swa.infrastructure.authorization;

public class AuthorizationResult {

    public final RepositoryStatus status;
    public enum RepositoryStatus {
        OK,
        USER_NOT_AUTHENTICATED,
        ERROR
    }

    private AuthorizationResult(RepositoryStatus status) {
        this.status = status;
    }

    public static AuthorizationResult create(RepositoryStatus status) {
        return new AuthorizationResult(status);
    }

    public boolean ok(){
        return status.equals(RepositoryStatus.OK);
    }

    public RepositoryStatus get(){
        return this.status;
    }
}
