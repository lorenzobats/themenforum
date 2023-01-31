package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteTopicUseCase;
import de.hsos.swa.application.input.dto.in.DeleteTopicCommand;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeleteTopicService implementiert das Interface
 * DeleteTopicUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen eines Themas durch einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeleteTopicUseCase            Korrespondierende Input-Port für diesen Use Case
 * @see DeleteTopicCommand     Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see TopicRepository                 Verwendeter Output-Port zum Löschen des Themas
 * @see AuthorizationGateway       Verwendeter Output-Port zum Laden der Rolle des anfragenden Nutzers
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteTopicService implements DeleteTopicUseCase {
    @Inject
    UserRepository userRepository;

    @Inject
    TopicRepository topicRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Löscht ein Thema auf Basis der übergebenen Informationen.
     * @param request enthält Themen-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Topic> enthält gelöschtes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Topic> deleteTopic(DeleteTopicCommand request) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = userResult.get();

        AuthorizationResult<String> roleResult = this.authorizationGateway.getUserAuthRole(user.getId());
        if (roleResult.invalid()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.get();

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
