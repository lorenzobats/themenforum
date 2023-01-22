package de.hsos.swa.application;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.VoteCommentInputPort;
import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.domain.entity.Comment;
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
