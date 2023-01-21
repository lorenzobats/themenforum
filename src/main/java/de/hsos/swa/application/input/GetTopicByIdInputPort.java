package de.hsos.swa.application.input;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.GetTopicByIdInputPortRequest;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

public interface GetTopicByIdInputPort {
    Result<Topic> getTopicById(@Valid GetTopicByIdInputPortRequest request);
}
