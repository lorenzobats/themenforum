package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteCommentVoteInputPort;
import de.hsos.swa.application.input.dto.in.DeleteCommentVoteInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

public class DeleteCommentVoteUseCase implements DeleteCommentVoteInputPort {
    @Override
    public Result<Comment> deleteCommentVote(DeleteCommentVoteInputPortRequest request) {
        // 1. User via request.username und UserRepository
        // 2. Post via request.postId und PostRepository
        // 3. Comment via Post.findCommentById(request.commentId) holen
        // 4. Domain Service nutzen um Vote des Users an Kommentar zu loeschen
        // 5. Post via PostRepository saven und zurückgeben
        return null;
    }
}
