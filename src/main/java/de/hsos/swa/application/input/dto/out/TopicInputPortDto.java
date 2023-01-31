package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Topic;

public class TopicInputPortDto {
    public final Topic topic;
    public final Long numberOfPosts;
    public TopicInputPortDto(Topic topic, Long numberOfPosts) {
        this.topic = topic;
        this.numberOfPosts = numberOfPosts;
    }
}


