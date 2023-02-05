package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public interface TopicRepository {

    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    RepositoryResult<Topic> saveTopic(Topic topic);
    RepositoryResult<Topic> deleteTopic(UUID topicId);

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    RepositoryResult<List<TopicWithPostCountDto>> getAllTopics();
    RepositoryResult<List<TopicWithPostCountDto>> searchTopic(String searchString);
    RepositoryResult<Topic> getTopicById(UUID topicId);
    RepositoryResult<Topic> getTopicByTitle(String title);
}
