package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.GetAllTopicsInputPort;
import de.hsos.swa.application.output.persistence.TopicRepository;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class GetAllTopicsUseCase implements GetAllTopicsInputPort {

    @Inject
    TopicRepository topicRepository;

    @Override
    public Result<List<Topic>> getAllTopics() {
        Result<List<Topic>> topicsResult = topicRepository.getAllTopics();
        if (topicsResult.isSuccessful()) {
            return Result.success(topicsResult.getData());
        }
        return Result.error("Cannot find Topics");
    }
}
