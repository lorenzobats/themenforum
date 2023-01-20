package de.hsos.swa.application.input;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.GetUserByNameInputPortRequest;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

public interface GetUserByNameInputPort {
   Result<User> getUserByName(@Valid GetUserByNameInputPortRequest request);
}
