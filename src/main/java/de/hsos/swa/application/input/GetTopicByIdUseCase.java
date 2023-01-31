package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;
@InputPort
public interface GetTopicByIdUseCase {
    Result<Topic> getTopicById(@Valid GetTopicByIdQuery request);
}
