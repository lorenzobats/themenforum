package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.SearchTopicsInputPort;
import de.hsos.swa.application.input.dto.in.SearchTopicsInputPortRequest;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.util.Result;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class SearchTopicsUseCase implements SearchTopicsInputPort {

    @Inject
    TopicRepository topicRepository;

    @Override
    public Result<List<TopicInputPortDto>> searchTopics(SearchTopicsInputPortRequest request) {
        Result<List<TopicInputPortDto>> topicsResult = topicRepository.searchTopic(request.searchString());
        if (!topicsResult.isSuccessful()) {
            return Result.error("Could not find Topics like (" + request.searchString() +")");
        }
        return Result.success(topicsResult.getData());
    }
}
