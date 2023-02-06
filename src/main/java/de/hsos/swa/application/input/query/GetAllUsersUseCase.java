package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;

import java.util.List;
@InputPort
public interface GetAllUsersUseCase {
    ApplicationResult<List<User>> getAllUsers(String requestingUser);
}
