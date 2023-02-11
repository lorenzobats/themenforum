package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.SearchTopicsUseCase;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Die Application Service Klasse SearchTopicsService implementiert das Interface
 * SearchTopicsUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Suchen nach Themen aus dem Topic-Repository.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see SearchTopicsUseCase                 Korrespondierender Input-Port für diesen Service
 * @see SearchTopicsUseCase                 Korrespondierender Input-Port für diesen Service
 * @see TopicRepository                     Output-Port zum Laden der Themen
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class SearchTopicsService implements SearchTopicsUseCase {

    @Inject
    TopicRepository topicRepository;

    /**
     * Lädt alle Themen aus dem Topic-Repository, die zum übergebenen Search-String passen.
     *
     * @param query enthält einen Search-String zur Suche nach Themen
     * @return ApplicationResult<List<TopicWithPostCountDto>> enthält die Liste aller Themen des Themenforums,
     * inklusive der Anzahl der Posts je Thema, welche zur Suchanfrage passen.
     */
    @Override
    public ApplicationResult<List<TopicWithPostCountDto>> searchTopics(SearchTopicsQuery query) {
        RepositoryResult<List<TopicWithPostCountDto>> result = topicRepository.searchTopic(query.searchString());
        if (result.error())
            return ApplicationResult.exception();

        return ApplicationResult.ok(result.get());
    }
}
