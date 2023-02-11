package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetTopicByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die Application Service Klasse GetTopicByIdService implementiert das Interface
 * GetTopicByIdUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden des Topics mit der übergebenen ID
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetTopicByIdUseCase                         Korrespondierender Input-Port für diesen Service
 * @see GetTopicByIdQuery                           Korrespondierendes Request-DTO für diesen Service
 * @see TopicRepository                             Output-Port zum Laden des Topics
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetTopicByIdService implements GetTopicByIdUseCase {

    @Inject
    TopicRepository topicRepository;

    /**
     * Lädt das Topic zu der übergebenen ID aus dem Topic-Repository
     *
     * @param query enthält die Topic-ID des Themas, der geladen werden soll
     * @return ApplicationResult<Topic> das gefundene Topic, oder alternativ Fehlermeldung
     */
    @Override
    public ApplicationResult<Topic> getTopicById(GetTopicByIdQuery query) {
        RepositoryResult<Topic> result = topicRepository.getTopicById(UUID.fromString(query.topicId()));
        if (result.error()) {
            if (result.status() == RepositoryResult.Status.ENTITY_NOT_FOUND) {
                return ApplicationResult.notFound("Cannot find topic: " + query.topicId());
            }
            return ApplicationResult.exception();
        }
        return ApplicationResult.ok(result.get());
    }
}
