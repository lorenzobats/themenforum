package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Topic;

public class TopicWithPostCountDto {

    public Topic topic;
    public Long numberOfPosts;

    public TopicWithPostCountDto(Topic topic, Long numberOfPosts) {
        this.topic = topic;
        this.numberOfPosts = numberOfPosts;
    }
}
