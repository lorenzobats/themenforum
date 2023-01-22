package de.hsos.swa.application.commands;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.VoteCommentInputPort;
import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.service.VoteService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
// TODO: Funktioniert nicht, Beziehung zum Parent Comment geht verloren
// Loesung -> Über Post gehen.
@ApplicationScoped
public class VoteCommentUseCase implements VoteCommentInputPort {

    @Inject
    PostRepository postRepository;

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


        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.getPostId()), true);
        if (!postResult.isSuccessful()) {
            return Result.error("Post does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        Post post = postResult.getData();

        // TODO: Vernünftiges Error Handling / Messaging / Oder einfach PUT = Idempotent?
        try {
            this.voteService.voteComment(post, user, request.getCommentId(), request.getVoteType());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        Result<Post> updatePostResult = this.postRepository.updatePost(post);
        if (!updatePostResult.isSuccessful()) {
            return Result.error("Updating Comment went wrong " + updatePostResult.getErrorMessage());
        }

        Optional<Comment> optionalComment = post.findCommentById(request.getCommentId());
        if(optionalComment.isEmpty()){
            return Result.error("Updating Comment went wrong " + updatePostResult.getErrorMessage());
        }
        return Result.success(optionalComment.get());
    }
}
