package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;

import java.util.List;
@InputPort
public interface GetAllTopicsUseCase {
    Result<List<TopicInputPortDto>> getAllTopics();
}
