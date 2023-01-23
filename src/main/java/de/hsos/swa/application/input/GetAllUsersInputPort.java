package de.hsos.swa.application.input;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

import java.util.List;

public interface GetAllUsersInputPort {
    Result<List<User>> getAllUsers();
}
