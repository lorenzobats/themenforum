package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllTopicsUseCase;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllTopicsService implements GetAllTopicsUseCase {

    @Inject
    TopicRepository topicRepository;

    @Override
    public Result<List<TopicWithPostCountDto>> getAllTopics() {
        RepositoryResult<List<TopicWithPostCountDto>> topicsResult = topicRepository.getAllTopics();
        if (topicsResult.badResult()) {
            return Result.error("Could not get Topics");
        }

        return Result.success(topicsResult.get());
    }
}
