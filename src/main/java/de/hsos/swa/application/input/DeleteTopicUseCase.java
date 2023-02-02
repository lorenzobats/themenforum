package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteTopicCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;
import java.util.Optional;

@InputPort
public interface DeleteTopicUseCase {
    ApplicationResult<Optional<Topic>> deleteTopic(@Valid DeleteTopicCommand request);
}
