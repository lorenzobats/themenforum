package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.application.annotations.InputPortDTO;
import de.hsos.swa.domain.entity.Topic;

@InputPortDTO
public class TopicWithPostCountDto {
    public final Topic topic;
    public final Long numberOfPosts;
    public TopicWithPostCountDto(Topic topic, Long numberOfPosts) {
        this.topic = topic;
        this.numberOfPosts = numberOfPosts;
    }
}


