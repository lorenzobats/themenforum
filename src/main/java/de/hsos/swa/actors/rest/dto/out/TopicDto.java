package de.hsos.swa.actors.rest.dto.out;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.domain.entity.Topic;

import java.time.LocalDateTime;

public class TopicDto {
    public String id;
    public String title;
    public String description;

    public LocalDateTime createdAt;
    public String owner;
    public Long numberOfPosts;


    public TopicDto(String id, String title, String description, LocalDateTime createdAt, String owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.owner = owner;
    }

    public TopicDto(String id, String title, String description, LocalDateTime createdAt, String owner, Long postCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.owner = owner;
        this.numberOfPosts = postCount;
    }

    public static class Converter {
        public static TopicDto fromDomainEntity(Topic topic) {
            return new TopicDto(topic.getId().toString(), topic.getTitle(), topic.getDescription(), topic.getCreatedAt(), topic.getOwner().getName());
        }

        public static TopicDto fromInputPortDto(TopicWithPostCountDto dto) {
            return new TopicDto(dto.topic.id, dto.topic.title, dto.topic.description, dto.topic.createdAt, dto.topic.owner, dto.numberOfPosts);
        }
    }
}
