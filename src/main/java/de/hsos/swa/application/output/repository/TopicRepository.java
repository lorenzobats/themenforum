package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;
import java.util.UUID;

/**
 * TODO: JavaDocs
 */
public interface TopicRepository {

    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    RepositoryResult<Topic> saveTopic(Topic topic);
    RepositoryResult<Topic> updateTopic(Topic topic);
    RepositoryResult<Topic> deleteTopic(UUID topicId);

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    RepositoryResult<List<TopicInputPortDto>> getAllTopics();
    RepositoryResult<List<TopicInputPortDto>> searchTopic(String searchString);
    RepositoryResult<Topic> getTopicById(UUID topicId);
}
