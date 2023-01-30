package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.SearchTopicsInputPortRequest;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;
import java.util.List;

public interface SearchTopicsInputPort {
   Result<List<TopicWithPostCountDto>> searchTopics(@Valid SearchTopicsInputPortRequest request);
}
