package de.hsos.swa.infrastructure.rest.dto.out;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.domain.entity.Topic;

public class TopicDto {
    public String id;
    public String title;
    public String description;
    public String owner;

    public Long posts;


    public TopicDto(String id, String title, String description, String owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public TopicDto(String id, String title, String description, String owner, Long posts) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.posts = posts;
    }

    public static class Converter {
        public static TopicDto toDto(Topic topic) {
            return new TopicDto(topic.getId().toString(), topic.getTitle(), topic.getDescription(), topic.getOwner().getName());
        }

        public static TopicDto toDto(TopicWithPostCountDto topic) {
            Long posts = topic.numberOfPosts;
            return new TopicDto(topic.id, topic.title, topic.description, topic.owner, topic.numberOfPosts);
        }
    }
}
