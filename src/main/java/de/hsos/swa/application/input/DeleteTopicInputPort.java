package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.DeleteTopicInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

public interface DeleteTopicInputPort {
    Result<Topic> deleteTopic(@Valid DeleteTopicInputPortRequest request);
}
