package de.hsos.swa.application.port.input.getUserByName;

import de.hsos.swa.application.port.input._shared.Result;

import javax.validation.Valid;

public interface GetUserByNameInputPort {
   Result<GetUserByNameInputPortResponse> getUserByName(@Valid GetUserByNameInputPortRequest inputPortRequest);
}
