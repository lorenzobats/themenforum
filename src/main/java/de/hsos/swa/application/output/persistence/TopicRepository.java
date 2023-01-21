package de.hsos.swa.application.output.persistence;

import de.hsos.swa.application.Result;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;
import java.util.UUID;

public interface TopicRepository {

    Result<List<Topic>> getAllTopics();

    Result<Topic> saveTopic(Topic topic);

    Result<Topic> getTopicById(UUID topicId);
}
