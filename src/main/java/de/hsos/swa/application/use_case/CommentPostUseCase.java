package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPort;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortRequest;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortResponse;
import de.hsos.swa.application.port.output.PostRepository;
import de.hsos.swa.application.port.output.user.GetUserByNameOutputPort;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class CommentPostUseCase implements CommentPostInputPort {

    @Inject
    PostRepository postRepository;

    @Inject
    GetUserByNameOutputPort getUserByNameOutputPort;


    @Override
    public Result<CommentPostInputPortResponse> commentPost(CommentPostInputPortRequest command) {
        // 1. Nutzer holen
        Result<User> getUserResponse = this.getUserByNameOutputPort.getUserByName(command.getUsername());
        if (!getUserResponse.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        User user = getUserResponse.getData();

        // 2. Kommentar fuer Nutzer erstellen
        // TODO: UUID klasse hinzufügen
        Comment comment = new Comment(user, command.getCommentText());

        // 3. Post holen
        Result<Post> getPostResponse = this.postRepository.getPostById(UUID.fromString(command.getPostId()), true);
        if (!getPostResponse.isSuccessful()) {
            return Result.error("Post does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        Post post = getPostResponse.getData();

        post.addComment(comment);

        // 4. Post persistieren
        Result<UUID> updatePostResponse = this.postRepository.updatePost(post);

        if (updatePostResponse.isSuccessful()) {
            return Result.success(new CommentPostInputPortResponse(updatePostResponse.getData()));
        }

        return Result.error("Something went wrong " + updatePostResponse.getErrorMessage());
    }
}
