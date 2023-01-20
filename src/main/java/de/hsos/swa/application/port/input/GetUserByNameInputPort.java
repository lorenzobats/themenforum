package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.GetUserByNameInputPortRequest;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

public interface GetUserByNameInputPort {
   Result<User> getUserByName(@Valid GetUserByNameInputPortRequest request);
}
