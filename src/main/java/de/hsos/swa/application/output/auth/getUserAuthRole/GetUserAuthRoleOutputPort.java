package de.hsos.swa.application.output.auth.getUserAuthRole;

import de.hsos.swa.application.util.Result;

import java.util.UUID;

public interface GetUserAuthRoleOutputPort {
    Result<String> getUserAuthRole(UUID userId);
}
