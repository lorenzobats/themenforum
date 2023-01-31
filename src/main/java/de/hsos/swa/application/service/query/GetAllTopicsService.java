package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllTopicsUseCase;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.input.dto.out.Result;

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
    public Result<List<TopicInputPortDto>> getAllTopics() {
        Result<List<TopicInputPortDto>> topicsResult = topicRepository.getAllTopicsWithPostCount();
        if (!topicsResult.isSuccessful()) {
            return Result.error("Could not get Topics");
        }

        return Result.success(topicsResult.getData());
    }
}
