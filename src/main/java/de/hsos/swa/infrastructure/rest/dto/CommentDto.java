package de.hsos.swa.infrastructure.rest.dto;

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

    public Integer voting;

    public CommentDto(String id, String username, String text, Integer voting) {
        this.id = id;
        this.username = username;
        this.text = text;
        this.voting = voting;
    }

    public String getId() {
        return this.id;
    }

    public static class Converter {
        public static CommentDto toDto(Comment comment) {
            List<CommentDto> repliesDto = comment.getReplies().stream().map(CommentDto.Converter::toDto).collect(Collectors.toList());

            CommentDto commentDto = new CommentDto(
                    String.valueOf(comment.getId()),
                    comment.getUser().getName(),
                    comment.getText(),
                    comment.getVoteSum());

            commentDto.replies = repliesDto;

            if(comment.getParentComment() != null) {
                commentDto.parentId = comment.getParentComment().getId().toString();
            }
            return commentDto;
        }

    }
}
