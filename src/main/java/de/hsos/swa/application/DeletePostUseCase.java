package de.hsos.swa.application;

import de.hsos.swa.application.input.DeletePostInputPort;
import de.hsos.swa.application.input.dto.in.DeletePostInputPortRequest;
import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class DeletePostUseCase implements DeletePostInputPort {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    GetUserAuthRoleOutputPort userAuthRoleOutputPort;


    @Override
    public Result<Post> deletePost(DeletePostInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.getUsername());
        if (!userResult.isSuccessful()) {
            return Result.error("User does not exist");
        }
        User user = userResult.getData();

        Result<String> roleResult = this.userAuthRoleOutputPort.getUserAuthRole(user.getId());
        if (!roleResult.isSuccessful()) {
            return Result.error("User Auth does not exist");
        }
        String role = roleResult.getData();

        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.getId()), false);
        if (!postResult.isSuccessful()) {
            return Result.error("Post does not exist");
        }
        Post post = postResult.getData();


        if(Objects.equals(role, "admin")){
            Result<Post> deletePostResult = this.postRepository.deletePost(post.getId());
            if (deletePostResult.isSuccessful()) {
                return Result.success(deletePostResult.getData());
            }
        }

        if(!post.getCreator().getId().equals(user.getId())){
            return Result.error("Not allowed to delete Post ");
        }

        Result<Post> deletePostResult = this.postRepository.deletePost(post.getId());
        if (deletePostResult.isSuccessful()) {
            return Result.success(deletePostResult.getData());
        }

        return Result.error("Something went wrong " + deletePostResult.getErrorMessage());
    }
}
