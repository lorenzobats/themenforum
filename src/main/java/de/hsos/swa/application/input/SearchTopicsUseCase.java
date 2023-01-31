package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.input.dto.out.Result;

import javax.validation.Valid;
import java.util.List;
@InputPort
public interface SearchTopicsUseCase {
   Result<List<TopicInputPortDto>> searchTopics(@Valid SearchTopicsQuery request);
}
