package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.CreateTopicInputPort;
import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.TopicFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;


/**
 * Die UseCase Klasse CreateTopicUseCase implementiert das Interface
 * CreateTopicInputPort der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Anlegen eines neuen Themas durch den Nutzer.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see CreateTopicInputPort            Korrespondierende Input-Port für diesen Use Case
 * @see CreateTopicInputPortRequest     Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see TopicRepository                 Verwendeter Output-Port zum Speichern des erzeugten Themas
 */

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class CreateTopicUseCase implements CreateTopicInputPort {

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
    public Result<Topic> createTopic(CreateTopicInputPortRequest request) {
        Result<User> getUserByNameResponse = this.userRepository.getUserByName(request.username());
        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = getUserByNameResponse.getData();

        Topic topic = TopicFactory.createTopic(request.title(), request.description(), user);

        Result<Topic> saveTopicResult = this.topicRepository.saveTopic(topic);
        if (!saveTopicResult.isSuccessful()) {
            return Result.error("Cannot create topic ");
        }
        return Result.success(saveTopicResult.getData());
    }
}
