package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.actors.rest.dto.out.TopicDto;
import de.hsos.swa.domain.entity.Topic;


public class TopicWithPostCountDto {
    public final TopicDto topic;
    public final Long numberOfPosts;
    public TopicWithPostCountDto(Topic topic, Long numberOfPosts) {
        this.topic = TopicDto.Converter.fromDomainEntity(topic);
        this.numberOfPosts = numberOfPosts;
    }
}


