package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.util.Result;

import java.util.List;

public interface GetAllTopicsWithPostCountInputPort {
    Result<List<TopicInputPortDto>> getAllTopics();
}
