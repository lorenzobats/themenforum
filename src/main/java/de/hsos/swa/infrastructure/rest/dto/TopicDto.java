package de.hsos.swa.infrastructure.rest.dto;
import de.hsos.swa.domain.entity.Topic;

public class TopicDto {
    public String id;
    public String title;
    public String description;
    public String owner;

    public TopicDto(String id, String title, String description, String owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public static class Converter {
        public static TopicDto toDto(Topic topic) {
            return new TopicDto(topic.getId().toString(), topic.getTitle(), topic.getDescription(), topic.getOwner().getName());
        }
    }
}
