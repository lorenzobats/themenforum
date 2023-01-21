package de.hsos.swa.application.use_case;

import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.CreatePostInputPort;
import de.hsos.swa.application.input.CreateTopicInputPort;
import de.hsos.swa.application.input.request.CreatePostInputPortRequest;
import de.hsos.swa.application.input.request.CreateTopicInputPortRequest;
import de.hsos.swa.application.output.persistence.PostRepository;
import de.hsos.swa.application.output.persistence.TopicRepository;
import de.hsos.swa.application.output.persistence.UserRepository;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;
import de.hsos.swa.domain.factory.TopicFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CreateTopicUseCase implements CreateTopicInputPort {

    @Inject
    UserRepository userRepository;

    @Inject
    TopicRepository topicRepository;


    @Override
    public Result<Topic> createTopic(CreateTopicInputPortRequest request) {
        Result<User> getUserByNameResponse = this.userRepository.getUserByName(request.getUsername());
        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("Topic could not be created");
        }
        User user = getUserByNameResponse.getData();

        Topic topic = TopicFactory.createTopic(request.getTitle(), request.getDescription(), user);

        Result<Topic> saveTopicResponse = this.topicRepository.saveTopic(topic);

        return Result.success(saveTopicResponse.getData());
    }
}
