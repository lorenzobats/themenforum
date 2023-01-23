package de.hsos.swa.application.input;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.input.dto.in.GetUserByNameInputPortRequest;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

public interface GetUserByNameInputPort {
   Result<User> getUserByName(@Valid GetUserByNameInputPortRequest request);
}
