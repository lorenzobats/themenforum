package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteTopicInputPort;
import de.hsos.swa.application.input.dto.in.DeleteTopicInputPortRequest;
import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeleteTopicUseCase implementiert das Interface
 * DeleteTopicInputPort der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen eines Themas durch einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeleteTopicInputPort            Korrespondierende Input-Port für diesen Use Case
 * @see DeleteTopicInputPortRequest     Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see TopicRepository                 Verwendeter Output-Port zum Löschen des Themas
 * @see GetUserAuthRoleOutputPort       Verwendeter Output-Port zum Laden der Rolle des anfragenden Nutzers
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class DeleteTopicUseCase implements DeleteTopicInputPort {
    @Inject
    UserRepository userRepository;

    @Inject
    TopicRepository topicRepository;

    @Inject
    GetUserAuthRoleOutputPort userAuthRoleOutputPort;

    /**
     * Löscht ein Thema auf Basis der übergebenen Informationen.
     * @param request enthält Themen-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Topic> enthält gelöschtes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Topic> deleteTopic(DeleteTopicInputPortRequest request) {
        Result<User> userResult = this.userRepository.getUserByName(request.username());
        if (!userResult.isSuccessful()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = userResult.getData();

        Result<String> roleResult = this.userAuthRoleOutputPort.getUserAuthRole(user.getId());
        if (!roleResult.isSuccessful()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.getData();

        if(!role.equals("admin")){
            return Result.error("Not allowed to delete post");
        }

        Result<Topic> deleteTopicResult = this.topicRepository.deleteTopic(UUID.fromString(request.id()));
        if (!deleteTopicResult.isSuccessful()) {
            return Result.error("Cannot delete topic");
        }

        return Result.success(deleteTopicResult.getData());
    }
}
