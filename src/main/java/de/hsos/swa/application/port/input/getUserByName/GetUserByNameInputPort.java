package de.hsos.swa.application.port.input.getUserByName;

import de.hsos.swa.application.port.input._shared.Result;

public interface GetUserByNameInputPort {
   Result<GetUserByNameInputPortResponse> getUserByName(GetUserByNameInputPortRequest inputPortRequest);
}
