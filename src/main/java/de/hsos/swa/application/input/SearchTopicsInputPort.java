package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.SearchTopicsInputPortRequest;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.util.Result;

import javax.validation.Valid;
import java.util.List;

public interface SearchTopicsInputPort {
   Result<List<TopicInputPortDto>> searchTopics(@Valid SearchTopicsInputPortRequest request);
}
