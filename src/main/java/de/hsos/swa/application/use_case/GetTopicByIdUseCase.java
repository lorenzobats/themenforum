package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.GetPostByIdInputPort;
import de.hsos.swa.application.input.GetTopicByIdInputPort;
import de.hsos.swa.application.input.request.GetPostByIdInputPortRequest;
import de.hsos.swa.application.input.request.GetTopicByIdInputPortRequest;
import de.hsos.swa.application.output.persistence.PostRepository;
import de.hsos.swa.application.output.persistence.TopicRepository;
import de.hsos.swa.domain.entity.Post;
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
