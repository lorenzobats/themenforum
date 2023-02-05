package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteTopicUseCase;
import de.hsos.swa.application.input.dto.in.DeleteTopicCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeleteTopicService implementiert das Interface
 * DeleteTopicUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen eines Themas durch einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeleteTopicUseCase              Korrespondierende Input-Port für diesen Use Case
 * @see DeleteTopicCommand              Korrespondierende Request DTO für diesen Use Case
 * @see TopicRepository                 Output-Port zum Löschen des Themas
 * @see AuthorizationGateway            Output-Port zur Zugriffskontrolle für Löschvorgang
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteTopicService implements DeleteTopicUseCase {

    @Inject
    TopicRepository topicRepository;
    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Löscht ein Thema auf Basis der übergebenen Informationen.
     *
     * @param command           enthält ID des zu löschenden Topics
     * @param requestingUser    enthält den Nutzernamen der Löschen-Anfrage
     * @return ApplicationResult<Topic> enthält gelöschtes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Optional<Topic>> deleteTopic(DeleteTopicCommand command, String requestingUser) {
        RepositoryResult<Topic> existingTopic = topicRepository.getTopicById(UUID.fromString(command.id()));
        if(existingTopic.error())
            return ApplicationResult.noContent(Optional.empty());
        
        AuthorizationResult<Boolean> permission = authorizationGateway.canDeleteTopic(requestingUser, UUID.fromString(command.id()));
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());

        RepositoryResult<Topic> result = this.topicRepository.deleteTopic(UUID.fromString(command.id()));
        if (result.error())
            return ApplicationResult.exception("Cannot delete post");

        return ApplicationResult.ok(Optional.of(result.get()));
    }
}
