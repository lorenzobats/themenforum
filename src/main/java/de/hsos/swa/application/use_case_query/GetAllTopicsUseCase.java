package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetAllTopicsInputPort;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class GetAllTopicsUseCase implements GetAllTopicsInputPort {

    @Inject
    TopicRepository topicRepository;

    @Inject
    Logger log;

    @Override
    public Result<List<Topic>> getAllTopics() {
        Result<List<Topic>> topicsResult = topicRepository.getAllTopics();
        if (topicsResult.isSuccessful()) {
            return Result.isSuccessful(topicsResult.getData());
        }

        Result<Map<UUID, Long>> postCountForAllTopics = topicRepository.getPostCountForAllTopics();
        if (topicsResult.isSuccessful()) {
            return Result.isSuccessful(topicsResult.getData());
        }

        return Result.error("Cannot find Topics");
    }
}