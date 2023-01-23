package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.UpdateTopicInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

public interface UpdateTopicInputPort {
   Result<Topic> updateTopic(@Valid UpdateTopicInputPortRequest request);
}
