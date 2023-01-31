package de.hsos.swa.application.output.auth;

import de.hsos.swa.application.annotations.OutputPort;
import de.hsos.swa.application.output.auth.dto.out.SaveAuthUserCommand;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import java.util.UUID;

@OutputPort
public interface AuthorizationGateway {
    AuthorizationResult<Void> createUserAuth(SaveAuthUserCommand outputPortRequest);
    AuthorizationResult<String> getUserAuthRole(UUID userId);
}
