package de.hsos.swa.application.input;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

public interface CreateTopicInputPort {
   Result<Topic> createTopic(@Valid CreateTopicInputPortRequest request);
}
