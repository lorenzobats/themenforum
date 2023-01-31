package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CreateTopicUseCase;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.input.dto.out.Result;
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
     * @param request enthält Titel, Text und Nutzername für das zu erstellende Thema
     * @return Result<Topic> enthält erstelltes Thema bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Topic> createTopic(CreateTopicCommand request) {
        RepositoryResult<User> getUserByNameResponse = this.userRepository.getUserByName(request.username());
        if(getUserByNameResponse.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = getUserByNameResponse.get();

        Topic topic = TopicFactory.createTopic(request.title(), request.description(), user);

        Result<Topic> saveTopicResult = this.topicRepository.saveTopic(topic);
        if (!saveTopicResult.isSuccessful()) {
            return Result.error("Cannot create topic ");
        }
        return Result.success(saveTopicResult.getData());
    }
}
