package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.VoteCommentInputPort;
import de.hsos.swa.application.input.VotePostInputPort;
import de.hsos.swa.application.input.request.VoteCommentInputPortRequest;
import de.hsos.swa.application.input.request.VotePostInputPortRequest;
import de.hsos.swa.application.output.persistence.CommentRepository;
import de.hsos.swa.application.output.persistence.PostRepository;
import de.hsos.swa.application.output.persistence.UserRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.service.VoteService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
// TODO: Funktioniert nicht, Beziehung zum Parent Comment geht verloren
// Loesung -> Über Post gehen.
@ApplicationScoped
public class VoteCommentUseCase implements VoteCommentInputPort {

    @Inject
    CommentRepository commentRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    VoteService voteService;


    @Override
    public Result<Comment> voteComment(VoteCommentInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.getUsername());
        if (!userResult.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        User user = userResult.getData();

        Result<Comment> commentResult = this.commentRepository.getCommentById(UUID.fromString(request.getCommentId()));
        if (!commentResult.isSuccessful()) {
            return Result.error("Comment does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        Comment comment = commentResult.getData();

        // TODO: Vernünftiges Error Handling / Messaging / Oder einfach PUT = Idempotent?
        try {
            this.voteService.voteComment(comment, user, request.getVoteType());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        Result<Comment> updatePostResult = this.commentRepository.updateComment(comment);


        if (updatePostResult.isSuccessful()) {
            return Result.success(updatePostResult.getData());
        }

        return Result.error("Something went wrong " + updatePostResult.getErrorMessage());

    }
}
