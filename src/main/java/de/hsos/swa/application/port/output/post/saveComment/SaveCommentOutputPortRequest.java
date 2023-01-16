package de.hsos.swa.application.port.output.post.saveComment;

import de.hsos.swa.domain.entity.Comment;

public class SaveCommentOutputPortRequest {

    private final Comment comment;

    public SaveCommentOutputPortRequest(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
