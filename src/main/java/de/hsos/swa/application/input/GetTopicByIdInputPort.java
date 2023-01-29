package de.hsos.swa.application.input;

import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.application.input.dto.in.GetTopicByIdInputPortRequest;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

public interface GetTopicByIdInputPort {
    Result<Topic> getTopicById(@Valid GetTopicByIdInputPortRequest request);
}
