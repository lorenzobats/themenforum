package de.hsos.swa.application.queries;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.GetTopicByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetTopicByIdInputPortRequest;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.UUID;

@RequestScoped
public class GetTopicByIdUseCase implements GetTopicByIdInputPort {

    @Inject
    TopicRepository topicRepository;

    @Override
    public Result<Topic> getTopicById(GetTopicByIdInputPortRequest request) {
        Result<Topic> topicResult = topicRepository.getTopicById(UUID.fromString(request.getId()));
        if (topicResult.isSuccessful()) {
            return Result.success(topicResult.getData());
        }
        return Result.error("Cannot find Topic");
    }
}