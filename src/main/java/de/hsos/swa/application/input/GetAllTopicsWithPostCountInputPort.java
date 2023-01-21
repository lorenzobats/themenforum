package de.hsos.swa.application.input;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.dto.TopicWithPostCountDto;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;

public interface GetAllTopicsWithPostCountInputPort {
    Result<List<TopicWithPostCountDto>> getAllTopics();
}
