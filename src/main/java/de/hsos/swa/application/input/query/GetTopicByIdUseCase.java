package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

/**
 * Das Interface GetTopicByIdUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetTopicByIdService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetTopicByIdService    Implementierender Service dieses Input Ports
 * @see GetTopicByIdQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface GetTopicByIdUseCase {
    ApplicationResult<Topic> getTopicById(@Valid GetTopicByIdQuery query);
}
