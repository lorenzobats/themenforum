package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.SearchTopicsUseCase;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;

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
    public ApplicationResult<List<TopicWithPostCountDto>> searchTopics(SearchTopicsQuery query) {
        RepositoryResult<List<TopicWithPostCountDto>> result = topicRepository.searchTopic(query.searchString());
        if (result.error()) {
            return ApplicationResult.exception();
        }
        return ApplicationResult.ok(result.get());
    }
}
