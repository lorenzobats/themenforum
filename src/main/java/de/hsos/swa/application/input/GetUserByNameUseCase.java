package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.GetUserByNameQuery;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;
import javax.ws.rs.core.SecurityContext;

@InputPort
public interface GetUserByNameUseCase {
   ApplicationResult<User> getUserByName(@Valid GetUserByNameQuery request, SecurityContext securityContext);
}
