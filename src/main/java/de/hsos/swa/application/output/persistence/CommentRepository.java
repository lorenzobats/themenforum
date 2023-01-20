package de.hsos.swa.application.output.persistence;

import de.hsos.swa.application.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;

import java.util.UUID;

public interface CommentRepository {
    Result<UUID> saveComment(Comment comment, Post post);
    Result<Comment> getCommentById(UUID commentId);
}
