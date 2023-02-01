package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.SearchTopicsUseCase;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class SearchTopicsService implements SearchTopicsUseCase {

    @Inject
    TopicRepository topicRepository;

    @Override
    public Result<List<TopicInputPortDto>> searchTopics(SearchTopicsQuery request) {
        RepositoryResult<List<TopicInputPortDto>> topicsResult = topicRepository.searchTopic(request.searchString());
        if (topicsResult.badResult()) {
            return Result.error("Could not find Topics like (" + request.searchString() +")");
        }
        return Result.success(topicsResult.get());
    }
}