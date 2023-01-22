package de.hsos.swa.infrastructure.rest.dto.out;
import de.hsos.swa.domain.entity.Post;

import javax.json.bind.annotation.JsonbProperty;
import java.util.List;

public class PostDto {
    public String id;

    public String title;
    public String content;
    public String creator;

    public TopicDto topic;
    @JsonbProperty(nillable = true)
    public List<CommentDto> comments;

    public Integer voting;

    public PostDto(String id, String title, String content, String creator, TopicDto topic, List<CommentDto> comments, Integer voting) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creator = creator;
        this.topic = topic;
        this.comments = comments;
        this.voting = voting;
    }

    public static class Converter {
        public static PostDto toDto(Post post) {
            List<CommentDto> comments = post.getComments().stream().map(CommentDto.Converter::toDto).toList();
            TopicDto topic = TopicDto.Converter.toDto(post.getTopic());
            return new PostDto(post.getId().toString(), post.getTitle(),  post.getContent(), post.getCreator().getName(), topic , comments, post.getVoteSum());
        }
    }
}
