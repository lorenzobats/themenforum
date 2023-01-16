package de.hsos.swa.application.port.output.user;


import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.domain.entity.User;

public interface GetUserByIdOutputPort {
    Result<User> getUserById(String userId);
}
