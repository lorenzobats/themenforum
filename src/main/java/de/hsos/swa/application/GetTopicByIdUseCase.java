package de.hsos.swa.application;

import de.hsos.swa.application.input.GetTopicByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetTopicByIdInputPortRequest;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.util.Result;
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
        Result<Topic> topicResult = topicRepository.getTopicById(UUID.fromString(request.id()));
        if (topicResult.isSuccessful()) {
            return Result.isSuccessful(topicResult.getData());
        }
        return Result.error("Cannot find Topic");
    }
}
