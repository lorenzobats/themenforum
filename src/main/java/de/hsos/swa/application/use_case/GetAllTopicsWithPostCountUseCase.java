package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.GetAllTopicsWithPostCountInputPort;
import de.hsos.swa.application.input.dto.TopicWithPostCountDto;
import de.hsos.swa.application.output.persistence.TopicRepository;
import de.hsos.swa.domain.entity.Topic;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static de.hsos.swa.application.input.dto.TopicWithPostCountDto.*;

@RequestScoped
public class GetAllTopicsWithPostCountUseCase implements GetAllTopicsWithPostCountInputPort {

    @Inject
    TopicRepository topicRepository;

    @Inject
    Logger log;

    @Override
    public Result<List<TopicWithPostCountDto>> getAllTopics() {
        Result<List<Topic>> topicsResult = topicRepository.getAllTopics();
        if (!topicsResult.isSuccessful()) {
            return Result.error("Could not get Topics");
        }

        Result<Map<UUID, Long>> postCountResult = topicRepository.getPostCountForAllTopics();
        if (!postCountResult.isSuccessful()) {
            return Result.error("Error Counting posts Topics");
        }

        List<TopicWithPostCountDto> result = topicsResult.getData().stream().map(t -> Converter.toDto(t, postCountResult.getData().get(t.getId()))).toList();

        return Result.success(result);
    }
}
