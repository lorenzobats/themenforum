package de.hsos.swa.infrastructure.rest.dto.out;

import de.hsos.swa.domain.entity.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {
    public String id;
    public String username;
    public String text;

    public String parentId;
    public List<CommentDto> replies = new ArrayList<>();

    public Integer downVoteCount;
    public Integer upVoteCount;

    public CommentDto(String id, String username, String text, Integer downVoteCount, Integer upVoteCount) {
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
        public static CommentDto fromDomainEntity(Comment comment) {
            List<CommentDto> repliesDto = comment.getReplies().stream().map(CommentDto.Converter::fromDomainEntity).collect(Collectors.toList());

            CommentDto commentDto = new CommentDto(
                    String.valueOf(comment.getId()),
                    comment.getUser().getName(),
                    comment.getText(),
                    comment.getDownVotes(),
                    comment.getUpVotes());

            commentDto.replies = repliesDto;

            if(comment.getParentComment() != null) {
                commentDto.parentId = comment.getParentComment().getId().toString();
            }
            return commentDto;
        }

//        public static CommentDto fromInputPortDto(CommentWithVoteCountDto comment) {
//            List<CommentDto> repliesDto = comment.replies.stream().map(CommentDto.Converter::fromInputPortDto).collect(Collectors.toList());
//
//            CommentDto commentDto = new CommentDto(
//                    String.valueOf(comment.id),
//                    comment.username,
//                    comment.text,
//                    comment.downVoteCount,
//                    comment.upVoteCount);
//
//            commentDto.replies = repliesDto;
//            commentDto.parentId = comment.parentId;
//
//            return commentDto;
//        }
    }
}
