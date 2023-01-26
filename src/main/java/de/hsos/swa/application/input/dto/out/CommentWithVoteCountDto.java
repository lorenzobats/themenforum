package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.domain.entity.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentWithVoteCountDto {
    public String id;
    public String username;
    public String text;

    public String parentId;
    public List<CommentWithVoteCountDto> replies = new ArrayList<>();

    public Integer downVoteCount;
    public Integer upVoteCount;

    public CommentWithVoteCountDto(String id, String username, String text, Integer downVoteCount, Integer upVoteCount) {
        this.id = id;
        this.username = username;
        this.text = text;
        this.downVoteCount = downVoteCount;
        this.upVoteCount = upVoteCount;
    }

    public String getId() {
        return this.id;
    }

    public static class Converter {
        public static CommentWithVoteCountDto fromDomainEntity(Comment comment) {
            List<CommentWithVoteCountDto> repliesDto = comment.getReplies().stream().map(CommentWithVoteCountDto.Converter::fromDomainEntity).collect(Collectors.toList());

            CommentWithVoteCountDto commentDto = new CommentWithVoteCountDto(
                    String.valueOf(comment.getId()),
                    comment.getUser().getName(),
                    comment.getText(),
                    comment.getDownvotes(),
                    comment.getUpvotes());

            commentDto.replies = repliesDto;

            if (comment.getParentComment() != null) {
                commentDto.parentId = comment.getParentComment().getId().toString();
            }
            return commentDto;
        }
    }
}
