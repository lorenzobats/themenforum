package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository {

    // READ
    Result<List<Comment>> getAllComments(boolean includeReplies);
    Result<Comment> getCommentById(UUID commentId, boolean includeReplies);
}
