package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.GetUserByNameQuery;
import de.hsos.swa.domain.entity.User;

import javax.validation.Valid;

/**
 * Das Interface GetUserByNameUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetUserByNameService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetUserByNameService    Implementierender Service dieses Input Ports
 * @see GetUserByNameQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetUserByNameUseCase {
   ApplicationResult<User> getUserByName(@Valid GetUserByNameQuery query, String requestingUser);
}
