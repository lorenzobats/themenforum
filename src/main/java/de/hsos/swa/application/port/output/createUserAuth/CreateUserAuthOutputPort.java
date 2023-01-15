package de.hsos.swa.application.port.output.createUserAuth;

import de.hsos.swa.application.port.input._shared.Result;

public interface CreateUserAuthOutputPort {
    Result<CreateUserAuthOutputPortResponse> createUserAuth(CreateUserAuthOutputPortRequest outputPortRequest);
}
