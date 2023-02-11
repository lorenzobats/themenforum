package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetAllTopicsUseCase;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Die Application Service Klasse GetAllTopicsService implementiert das Interface
 * GetAllTopicsUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden aller Themen aus dem Topic-Repository.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetAllTopicsUseCase                 Korrespondierender Input-Port für diesen Service
 * @see TopicRepository                     Output-Port zum Laden der Themen
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllTopicsService implements GetAllTopicsUseCase {

    @Inject
    TopicRepository topicRepository;

    /**
     * Lädt alle Themen aus dem Topic-Repository.
     *
     * @return ApplicationResult<List<TopicWithPostCountDto>> enthält die Liste aller Themen des Themenforums,
     * inklusive der Anzahl der Posts je Thema
     */
    @Override
    public ApplicationResult<List<TopicWithPostCountDto>> getAllTopics() {
        RepositoryResult<List<TopicWithPostCountDto>> topicsResult = topicRepository.getAllTopics();
        if (topicsResult.error())
            return ApplicationResult.exception();

        return ApplicationResult.ok(topicsResult.get());
    }
}
