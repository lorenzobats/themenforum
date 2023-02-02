package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

@InputPort
public interface CreateTopicUseCase {
   ApplicationResult<Topic> createTopic(@Valid CreateTopicCommand request, String securityContext);
}
