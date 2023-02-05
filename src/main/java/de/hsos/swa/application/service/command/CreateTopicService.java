package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CreateTopicUseCase;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.TopicFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;


/**
 * Die UseCase Klasse CreateTopicService implementiert das Interface
 * CreateTopicUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Anlegen eines neuen Themas durch den Nutzer.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see CreateTopicUseCase              Korrespondierende Input-Port für diesen Use Case
 * @see CreateTopicCommand              Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Output-Port zum Laden des anfragenden Nutzers
 * @see TopicRepository                 Output-Port zum Speichern des erzeugten Themas
 * @see AuthorizationGateway            Output-Port zum Speichern des Topic-Inhabers für spätere Zugriffskontrolle

 */

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class CreateTopicService implements CreateTopicUseCase {

    @Inject
    UserRepository userRepository;
    @Inject
    TopicRepository topicRepository;

    @Inject
    AuthorizationGateway authorizationGateway;


    /**
     * Erstellt ein neues Thema auf Basis der übergebenen Informationen.
     *
     * @param command           enthält Titel und Text für das zu erstellende Thema
     * @param requestingUser    enthält den Nutzernamen des Erstellers
     * @return ApplicationResult<Topic> enthält erstelltes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Topic> createTopic(CreateTopicCommand command, String requestingUser) {
        RepositoryResult<Topic> existingTopicResult = this.topicRepository.getTopicByTitle(command.title());
        if(existingTopicResult.ok())
                return ApplicationResult.notValid(command.title() + " Topic already exists");

        RepositoryResult<User> userResult = this.userRepository.getUserByName(requestingUser);
        if (userResult.error())
            return ApplicationResult.notValid("Cannot find user: " + requestingUser);
        User user = userResult.get();

        Topic topic = TopicFactory.createTopic(command.title(), command.description(), user);
        RepositoryResult<Topic> useCaseResult = this.topicRepository.saveTopic(topic);
        if (useCaseResult.error())
            return ApplicationResult.exception("Cannot create topic ");

        if(authorizationGateway.addOwnership(requestingUser, useCaseResult.get().getId()).denied())
            return ApplicationResult.exception("Cannot create topic");

        return ApplicationResult.ok(useCaseResult.get());
    }
}
