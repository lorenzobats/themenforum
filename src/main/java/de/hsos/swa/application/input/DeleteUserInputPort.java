package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.DeleteUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

public interface DeleteUserInputPort {
    Result<User> deleteUser(@Valid DeleteUserInputPortRequest request);
}
