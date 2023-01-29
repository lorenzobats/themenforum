package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Topic;

import java.time.LocalDateTime;

public class TopicWithEmbeddedPostCountDto {

    public Topic topic;
    public Long numberOfPosts;

    public TopicWithEmbeddedPostCountDto(Topic topic, Long numberOfPosts) {
        this.topic = topic;
        this.numberOfPosts = numberOfPosts;
    }
}
