package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;
import java.util.UUID;

/**
 * TODO: JavaDocs
 */
public interface TopicRepository {

    // COMMANDS
    Result<Topic> saveTopic(Topic topic);
    // TODO: UPDATE TOPIC
    Result<Topic> updateTopic(Topic topic);

    Result<Topic> deleteTopic(UUID topicId);

    // QUERIES
    Result<List<TopicInputPortDto>> getAllTopicsWithPostCount();

    Result<List<TopicInputPortDto>> searchTopic(String searchString);

    Result<Topic> getTopicById(UUID topicId);
}
