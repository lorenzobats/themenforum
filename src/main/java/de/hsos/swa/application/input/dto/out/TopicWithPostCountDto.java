package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Topic;

public class TopicWithPostCountDto {

    public String id;
    public String title;
    public String description;
    public String owner;
    public Long numberOfPosts;

    public TopicWithPostCountDto(String id, String title, String description, String owner, Long numberOfPosts) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.numberOfPosts = numberOfPosts;
    }

    public static class Converter {
        public static TopicWithPostCountDto toDto(Topic topic, Long numberOfPosts) {
            return new TopicWithPostCountDto(topic.getId().toString(), topic.getTitle(), topic.getDescription(), topic.getOwner().getName(), numberOfPosts);
        }
    }
}
