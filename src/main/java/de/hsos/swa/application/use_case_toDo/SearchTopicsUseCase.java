package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.SearchTopicsInputPort;
import de.hsos.swa.application.input.dto.in.SearchTopicsInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

public class SearchTopicsUseCase implements SearchTopicsInputPort {
    @Override
    public Result<Topic> searchTopics(SearchTopicsInputPortRequest request) {
        return null;
    }
}
