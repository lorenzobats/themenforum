package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.SearchTopicsInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.validation.Valid;

public interface SearchTopicsInputPort {
   Result<Topic> searchTopics(@Valid SearchTopicsInputPortRequest request);
}
