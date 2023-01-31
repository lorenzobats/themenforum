package de.hsos.swa.actors.rest.dto.out;
import de.hsos.swa.domain.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {
    public String id;

    public String title;
    public String content;
    public LocalDateTime createdAt;
    public String creator;

    public TopicDto topic;
    public List<CommentDto> comments;
    public Integer downvoteCount;
    public Integer upvoteCount;


    public PostDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public PostDto(String id, String title, String content, LocalDateTime createdAt, String creator, TopicDto topic, List<CommentDto> comments, Integer downvoteCount, Integer upvoteCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.creator = creator;
        this.topic = topic;
        this.comments = comments;
        this.downvoteCount = downvoteCount;
        this.upvoteCount = upvoteCount;
    }

    public static class Converter {
        public static PostDto fromDomainEntity(Post post) {
            List<CommentDto> comments = post.getComments().stream().map(CommentDto.Converter::fromDomainEntity).toList();
            TopicDto topic = TopicDto.Converter.fromDomainEntity(post.getTopic());
            return new PostDto(
                    post.getId().toString(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt(),
                    post.getUser().getName(),
                    topic ,
                    comments,
                    post.getDownvotes(),
                    post.getUpvotes());
        }
    }
}
