package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;

/**
 * Das Interface GetAllUsersUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetAllUsersService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetAllUsersService    Implementierender Service dieses Input Ports
 * @see GetAllUsersQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
import java.util.List;
@InputPort
public interface GetAllUsersUseCase {
    ApplicationResult<List<User>> getAllUsers(String requestingUser);
}
