package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;
import java.util.UUID;

public interface TopicRepository {

    // CREATE
    Result<Topic> saveTopic(Topic topic);

    // READ
    Result<List<TopicWithPostCountDto>> getAllTopicsWithPostCount();

    Result<List<TopicWithPostCountDto>> searchTopic(String searchString);

    Result<Topic> getTopicById(UUID topicId);


    // UPDATE


    // DELETE
    Result<Topic> deleteTopic(UUID topicId);
}
