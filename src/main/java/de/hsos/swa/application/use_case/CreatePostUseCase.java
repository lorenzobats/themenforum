package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPort;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortResponse;
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
    public Result<CreatePostInputPortResponse> createPost(CreatePostInputPortRequest inputPortRequest) {
        // 1. Nutzer holen
        Result<User> getUserByNameResponse = this.userRepository.getUserByName(inputPortRequest.getUsername());
        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("Post could not be created"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        User user = getUserByNameResponse.getData();

        // 2. Post auf Domain-Ebene bauen
        Post post = PostFactory.createPost(inputPortRequest.getTitle(), user);

        // 3. Post persistieren
        Result<UUID> savePostResponse = this.postRepository.savePost(post);

        return Result.success(new CreatePostInputPortResponse(savePostResponse.getData()));
    }
}
