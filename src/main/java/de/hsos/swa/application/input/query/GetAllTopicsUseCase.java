package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;

import java.util.List;
@InputPort
public interface GetAllTopicsUseCase {
    ApplicationResult<List<TopicWithPostCountDto>> getAllTopics();
}
