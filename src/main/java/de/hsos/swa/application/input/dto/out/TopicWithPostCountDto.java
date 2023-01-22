package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Topic;

import java.time.LocalDateTime;

public class TopicWithPostCountDto {

    public String id;
    public String title;
    public String description;
    public String owner;

    public LocalDateTime createdAt;
    public Long numberOfPosts;

    public TopicWithPostCountDto(String id, String title, String description, LocalDateTime createdAt, String owner, Long numberOfPosts) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.owner = owner;
        this.numberOfPosts = numberOfPosts;
    }

    public static class Converter {
        public static TopicWithPostCountDto toDto(Topic topic, Long numberOfPosts) {
            return new TopicWithPostCountDto(topic.getId().toString(), topic.getTitle(), topic.getDescription(), topic.getCreatedAt(), topic.getOwner().getName(), numberOfPosts);
        }
    }
}
