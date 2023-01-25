package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.UUID;

public interface CommentRepository {

    // READ
    Result<Comment> getCommentById(UUID commentId, boolean includeReplies);

    Result<List<Comment>> getAllComments(boolean includeReplies);
}
