package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.DeleteCommentInputPort;
import de.hsos.swa.application.input.dto.in.DeleteCommentInputPortRequest;
import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DeleteCommentUseCase implements DeleteCommentInputPort {
    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    GetUserAuthRoleOutputPort userAuthRoleOutputPort;


    @Override
    public Result<Comment> deleteComment(DeleteCommentInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.username());
        if (!userResult.isSuccessful()) {
            return Result.error("User does not exist");
        }
        User user = userResult.getData();

        Result<String> roleResult = this.userAuthRoleOutputPort.getUserAuthRole(user.getId());
        if (!roleResult.isSuccessful()) {
            return Result.error("User Auth does not exist");
        }
        String role = roleResult.getData();

        Result<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.id()));
        if (!postResult.isSuccessful()) {
            return Result.error("Post does not exist");
        }
        Post post = postResult.getData();


        Optional<Comment> optionalComment = post.findCommentById(request.id());
        if(optionalComment.isEmpty()){
            return Result.error("Comment does not exist");
        }

        Comment comment = optionalComment.get();

        // TODO: Kommentar auf deleted setzen
        comment.setText("deleted");

        if(Objects.equals(role, "admin")){
            Result<Post> updatePostPostResult = this.postRepository.updatePost(post);
            if (updatePostPostResult.isSuccessful()) {
                return Result.isSuccessful(comment); // TODO: Was zurückgeben?
            }
        }

        if(!comment.getUser().getId().equals(user.getId())){
            return Result.error("Not allowed to delete Comment");
        }

        Result<Post> deletePostResult = this.postRepository.deletePost(post.getId());
        if (deletePostResult.isSuccessful()) {
            Result<Post> updatePostPostResult = this.postRepository.updatePost(post);
            if (updatePostPostResult.isSuccessful()) {
                return Result.isSuccessful(comment); // TODO: Was zurückgeben?
            }
        }

        return Result.error("Something went wrong " + deletePostResult.getMessage());
    }
}
