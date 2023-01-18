package de.hsos.swa.adapter.input.rest.dto;

import de.hsos.swa.domain.entity.Comment;

public class CommentDto {
    public String id;
    public String username;
    public String text;

    public CommentDto(String id, String username, String text) {
        this.id = id;
        this.username = username;
        this.text = text;
    }

    public static class Converter {
        public static CommentDto toDto(Comment comment) {
            return new CommentDto(String.valueOf(comment.getId()), comment.getUser().getName(), comment.getText());
        }
    }
}
