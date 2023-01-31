package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;

import java.util.List;
import java.util.UUID;
/**
 * TODO: JavaDocs
 */
public interface CommentRepository {
    RepositoryResult<List<Comment>> getAllComments(boolean includeReplies);
    RepositoryResult<Comment> getCommentById(UUID commentId, boolean includeReplies);
}
