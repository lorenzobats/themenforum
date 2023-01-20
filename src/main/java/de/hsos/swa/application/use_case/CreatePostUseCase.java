package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.application.port.input.CreatePostInputPort;
import de.hsos.swa.application.port.input.request.CreatePostInputPortRequest;
import de.hsos.swa.application.port.output.PostRepository;
import de.hsos.swa.application.port.output.UserRepository;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class CreatePostUseCase implements CreatePostInputPort {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;


    @Override
    public Result<UUID> createPost(CreatePostInputPortRequest request) {
        Result<User> getUserByNameResponse = this.userRepository.getUserByName(request.getUsername());
        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("Post could not be created"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        User user = getUserByNameResponse.getData();

        Post post = PostFactory.createPost(request.getTitle(), user);

        Result<UUID> savePostResponse = this.postRepository.savePost(post);

        return Result.success(savePostResponse.getData());
    }
}
