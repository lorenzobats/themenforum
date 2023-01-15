package de.hsos.swa.application.port.output.user.saveUser;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.domain.entity.User;

import java.util.UUID;

public interface SaveUserOutputPort {
    Result<UUID> saveUser(User user);
}
