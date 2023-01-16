package de.hsos.swa.application.port.output.comment;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;

import java.util.UUID;

public interface SaveCommentOutputPort {
   Result<UUID> saveComment(Comment comment, Post post);
}
