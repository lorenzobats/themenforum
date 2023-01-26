package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.CreateTopicInputPort;
import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.TopicFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class CreateTopicUseCase implements CreateTopicInputPort {

    @Inject
    UserRepository userRepository;

    @Inject
    TopicRepository topicRepository;


    @Override
    public Result<Topic> createTopic(CreateTopicInputPortRequest request) {
        Result<User> getUserByNameResponse = this.userRepository.getUserByName(request.username());
        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("Topic could not be created");
        }
        User user = getUserByNameResponse.getData();

        Topic topic = TopicFactory.createTopic(request.title(), request.description(), user);

        Result<Topic> saveTopicResponse = this.topicRepository.saveTopic(topic);

        return Result.success(saveTopicResponse.getData());
    }
}
