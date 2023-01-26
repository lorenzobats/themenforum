package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.SearchTopicsInputPort;
import de.hsos.swa.application.input.dto.in.SearchTopicsInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class SearchTopicsUseCase implements SearchTopicsInputPort {
    @Override
    public Result<Topic> searchTopics(SearchTopicsInputPortRequest request) {
        return null;
    }
}
