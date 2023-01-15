package de.hsos.swa.application.port.output.createUser;

import de.hsos.swa.application.port.input._shared.Result;

public interface CreateUserOutputPort{
    Result<CreateUserOutputPortResponse> createUser(CreateUserOutputPortRequest outputPortRequest);
}
