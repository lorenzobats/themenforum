package de.hsos.swa.application.output.auth.createUserAuth;

import de.hsos.swa.application.input.Result;

public interface CreateUserAuthOutputPort {
    Result<CreateUserAuthOutputPortResponse> createUserAuth(CreateUserAuthOutputPortRequest outputPortRequest);
}
