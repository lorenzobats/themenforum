package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetAllTopicsWithPostCountInputPort;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.util.Result;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetAllTopicsWithPostCountUseCase implements GetAllTopicsWithPostCountInputPort {

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
