package de.hsos.swa.application.port.input.getCommentById;

import de.hsos.swa.domain.entity.Comment;

public class GetCommentByIdInputPortResponse {
    private Comment comment;

    public GetCommentByIdInputPortResponse(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
