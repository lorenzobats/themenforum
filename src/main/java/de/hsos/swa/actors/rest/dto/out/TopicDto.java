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
    public Long postCount;


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
        this.postCount = postCount;
    }

    public static class Converter {
        public static TopicDto fromDomainEntity(Topic topic) {
            return new TopicDto(topic.getId().toString(), topic.getTitle(), topic.getDescription(), topic.getCreatedAt(), topic.getOwner().getName());
        }

        public static TopicDto fromInputPortDto(TopicWithPostCountDto dto) {
            return new TopicDto(dto.topic.getId().toString(), dto.topic.getTitle(), dto.topic.getDescription(), dto.topic.getCreatedAt(), dto.topic.getOwner().getName(), dto.numberOfPosts);
        }
    }
}
