package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteTopicInputPort;
import de.hsos.swa.application.input.dto.in.DeleteTopicInputPortRequest;
import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class DeleteTopicUseCase implements DeleteTopicInputPort {

    @Inject
    TopicRepository topicRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    GetUserAuthRoleOutputPort userAuthRoleOutputPort;

    @Override
    public Result<Topic> deleteTopic(DeleteTopicInputPortRequest request) {
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

        if(!Objects.equals(role, "admin")){
            return Result.error("Not allowed to delete Post ");
        }

        Result<Topic> deleteTopicResult = this.topicRepository.deleteTopic(UUID.fromString(request.id()));

        if (!deleteTopicResult.isSuccessful()) {
            return Result.error("Could not Delete Topic " + deleteTopicResult.getMessage());
        }

        return Result.success(deleteTopicResult.getData());
    }
}
