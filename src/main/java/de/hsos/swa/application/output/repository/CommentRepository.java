package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import java.util.UUID;

public interface CommentRepository {
    Result<Comment> updateComment(Comment comment);
    Result<Comment> getCommentById(UUID commentId);
}
