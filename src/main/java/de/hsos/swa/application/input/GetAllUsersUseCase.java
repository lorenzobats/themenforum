package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;

import javax.ws.rs.core.SecurityContext;
import java.util.List;
@InputPort
public interface GetAllUsersUseCase {
    ApplicationResult<List<User>> getAllUsers(SecurityContext securityContext);
}
