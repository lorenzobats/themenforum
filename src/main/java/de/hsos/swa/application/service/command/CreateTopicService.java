package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CreateTopicUseCase;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
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
 * @see CreateTopicUseCase            Korrespondierende Input-Port für diesen Use Case
 * @see CreateTopicCommand     Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see TopicRepository                 Verwendeter Output-Port zum Speichern des erzeugten Themas
 */

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class CreateTopicService implements CreateTopicUseCase {

    @Inject
    UserRepository userRepository;
    @Inject
    TopicRepository topicRepository;


    /**
     * Erstellt ein neues Thema auf Basis der übergebenen Informationen.
     *
     * @param command         enthält Titel, Text und Nutzername für das zu erstellende Thema
     * @return ApplicationResult<Topic> enthält erstelltes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Topic> createTopic(CreateTopicCommand command, String requestingUser) {
        // TODO: überprüfen, ob schon vorhanden
        RepositoryResult<User> getUserByNameResponse = this.userRepository.getUserByName(command.username());
        if(getUserByNameResponse.error()) {
            return ApplicationResult.exception("Cannot find user " + command.username());
        }
        User user = getUserByNameResponse.get();

        Topic topic = TopicFactory.createTopic(command.title(), command.description(), user);

        RepositoryResult<Topic> saveTopicResult = this.topicRepository.saveTopic(topic);
        if (saveTopicResult.error()) {
            return ApplicationResult.exception("Cannot create topic ");
        }
        return ApplicationResult.ok(saveTopicResult.get());
    }
}
