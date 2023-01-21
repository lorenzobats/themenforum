package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.GetAllTopicsInputPort;
import de.hsos.swa.application.output.persistence.TopicRepository;
import de.hsos.swa.domain.entity.Topic;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestScoped
public class GetAllTopicsUseCase implements GetAllTopicsInputPort {

    @Inject
    TopicRepository topicRepository;

    @Inject
    Logger log;

    @Override
    public Result<List<Topic>> getAllTopics() {
        Result<List<Topic>> topicsResult = topicRepository.getAllTopics();
        if (topicsResult.isSuccessful()) {
            return Result.success(topicsResult.getData());
        }

        Result<Map<UUID, Long>> postCountForAllTopics = topicRepository.getPostCountForAllTopics();
        if (topicsResult.isSuccessful()) {
            return Result.success(topicsResult.getData());
        }

        return Result.error("Cannot find Topics");
    }
}
