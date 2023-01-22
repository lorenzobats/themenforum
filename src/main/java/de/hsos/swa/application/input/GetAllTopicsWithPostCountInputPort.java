package de.hsos.swa.application.input;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;

import java.util.List;

public interface GetAllTopicsWithPostCountInputPort {
    Result<List<TopicWithPostCountDto>> getAllTopics();
}
