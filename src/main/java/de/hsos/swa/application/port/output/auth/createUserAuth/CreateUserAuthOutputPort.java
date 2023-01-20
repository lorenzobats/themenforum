package de.hsos.swa.application.port.output.auth.createUserAuth;

import de.hsos.swa.application.port.input.Result;

public interface CreateUserAuthOutputPort {
    Result<CreateUserAuthOutputPortResponse> createUserAuth(CreateUserAuthOutputPortRequest outputPortRequest);
}
