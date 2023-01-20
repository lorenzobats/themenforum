package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.CreatePostInputPort;
import de.hsos.swa.application.input.request.CreatePostInputPortRequest;
import de.hsos.swa.application.output.UserRepository;
import de.hsos.swa.application.output.persistence.PostRepository;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CreatePostUseCase implements CreatePostInputPort {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;


    @Override
    public Result<Post> createPost(CreatePostInputPortRequest request) {
        Result<User> getUserByNameResponse = this.userRepository.getUserByName(request.getUsername());
        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("Post could not be created"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }
        User user = getUserByNameResponse.getData();

        Post post = PostFactory.createPost(request.getTitle(), user);

        Result<Post> savePostResponse = this.postRepository.savePost(post);

        return Result.success(savePostResponse.getData());
    }
}
