package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

/**
 * Das Interface CreateTopicUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * CreateTopicService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.CreateTopicService    Implementierender Service dieses Input Ports
 * @see CreateTopicCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface CreateTopicUseCase {
   ApplicationResult<Topic> createTopic(@Valid CreateTopicCommand command, String requestingUser);
}
