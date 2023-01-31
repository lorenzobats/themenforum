package de.hsos.swa.actors.rest.dto.out;

import de.hsos.swa.domain.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {
    public String id;
    public String username;
    public String text;

    public LocalDateTime createdAt;
    public String parentId;
    public List<CommentDto> replies = new ArrayList<>();
    public Integer downVoteCount;
    public Integer upVoteCount;

    public CommentDto(String id, String username, String text, LocalDateTime createdAt, Integer downVoteCount, Integer upVoteCount) {
        this.id = id;
        this.username = username;
        this.text = text;
        this.createdAt = createdAt;
        this.downVoteCount = downVoteCount;
        this.upVoteCount = upVoteCount;
    }

    public String getId() {
        return this.id;
    }

    public static class Converter {
        public static CommentDto fromDomainEntity(Comment comment) {
            List<CommentDto> repliesDto = comment.getReplies().stream().map(CommentDto.Converter::fromDomainEntity).collect(Collectors.toList());

            CommentDto commentDto = new CommentDto(
                    String.valueOf(comment.getId()),
                    comment.getUser().getName(),
                    comment.getText(),
                    comment.getCreatedAt(),
                    comment.getDownvotes(),
                    comment.getUpvotes());

            commentDto.replies = repliesDto;

            if(comment.getParentComment() != null) {
                commentDto.parentId = comment.getParentComment().getId().toString();
            }
            return commentDto;
        }
    }
}
