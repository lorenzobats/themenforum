package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteTopicCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Das Interface DeleteTopicUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * DeleteTopicService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.command.DeleteTopicService    Implementierender Service dieses Input Ports
 * @see DeleteTopicCommand                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface DeleteTopicUseCase {
    ApplicationResult<Optional<Topic>> deleteTopic(@Valid DeleteTopicCommand command, String requestingUser);
}
