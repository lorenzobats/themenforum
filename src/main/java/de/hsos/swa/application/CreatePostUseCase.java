package de.hsos.swa.application;

import de.hsos.swa.application.input.CreatePostInputPort;
import de.hsos.swa.application.input.dto.in.CreatePostInputPortRequest;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CreatePostUseCase implements CreatePostInputPort {

    @Inject
    UserRepository userRepository;

    @Inject
    TopicRepository topicRepository;
    @Inject
    PostRepository postRepository;


    @Override
    public Result<Post> createPost(CreatePostInputPortRequest request) {
        Result<User> getUserByNameResponse = this.userRepository.getUserByName(request.username());
        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("User " + request.username() + " does not exist");
        }
        User user = getUserByNameResponse.getData();


        Result<Topic> getTopicByIdResponse = this.topicRepository.getTopicById(request.topicId());
        if(!getTopicByIdResponse.isSuccessful()) {
            return Result.error("Topic with ID" + request.topicId() +  "does not exist");
        }
        Topic topic = getTopicByIdResponse.getData();


        Post post = PostFactory.createPost(request.title(), request.content(), topic, user);

        Result<Post> savePostResponse = this.postRepository.savePost(post);

        return Result.isSuccessful(savePostResponse.getData());
    }
}
