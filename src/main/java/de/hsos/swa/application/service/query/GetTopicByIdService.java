package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetTopicByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetTopicByIdService implements GetTopicByIdUseCase {

    @Inject
    TopicRepository topicRepository;

    @Override
    public Result<Topic> getTopicById(GetTopicByIdQuery request) {
        Result<Topic> topicResult = topicRepository.getTopicById(UUID.fromString(request.topicId()));
        if (topicResult.isSuccessful()) {
            return Result.success(topicResult.getData());
        }
        return Result.error("Cannot find Topic");
    }
}
