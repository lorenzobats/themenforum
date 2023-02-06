package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;
@InputPort
public interface GetTopicByIdUseCase {
    ApplicationResult<Topic> getTopicById(@Valid GetTopicByIdQuery query);
}
