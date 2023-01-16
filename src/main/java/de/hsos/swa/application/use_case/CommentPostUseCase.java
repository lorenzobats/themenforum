package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPort;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortCommand;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortResult;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortResponse;
import de.hsos.swa.application.port.output.comment.SaveCommentOutputPort;
import de.hsos.swa.application.port.output.post.GetPostByIdOutputPort;
import de.hsos.swa.application.port.output.user.GetUserByIdOutputPort;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class CommentPostUseCase implements CommentPostInputPort {

    @Inject
    GetUserByIdOutputPort getUserByIdOutputPort;

    @Inject
    GetPostByIdOutputPort getPostByIdOutputPort;

    @Inject
    SaveCommentOutputPort saveCommentOutputPort;


    @Override
    public Result<CommentPostInputPortResult> commentPost(CommentPostInputPortCommand command) {
        // 1. Nutzer holen
        Result<User> getUserByIdResponse = this.getUserByIdOutputPort.getUserById(command.getUserId());
        if(!getUserByIdResponse.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        User user = getUserByIdResponse.getData();

        // 2. Kommentar fuer Nutzer erstellen
        // TODO: UUID klasse hinzufügen
        Comment comment = new Comment(user, command.getCommentText());

        // 3. Post holen
        Result<Post> getPostByIdResponse = this.getPostByIdOutputPort.getPostById(UUID.fromString(command.getPostId()));
        if(!getPostByIdResponse.isSuccessful()) {
            return Result.error("Post does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        Post post = getPostByIdResponse.getData();

        // 4. Post persistieren
        Result<UUID> savePostResponse = this.saveCommentOutputPort.saveComment(comment, post);  // ToDo: Komisch :D

        return Result.success(new CommentPostInputPortResult(savePostResponse.getData()));
    }
}
