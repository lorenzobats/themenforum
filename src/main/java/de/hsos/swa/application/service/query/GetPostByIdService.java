package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetPostByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.service.SortByDate;
import de.hsos.swa.domain.service.SortByUpvotes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Die Application Service Klasse GetPostByIdService implementiert das Interface
 * GetPostByIdUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden des Posts mit der übergebenen ID
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetPostByIdUseCase                      Korrespondierender Input-Port für diesen Service
 * @see GetPostByIdQuery                        Korrespondierendes Request-DTO für diesen Service
 * @see PostRepository                          Output-Port zum Laden des Posts
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetPostByIdService implements GetPostByIdUseCase {

    @Inject
    PostRepository postRepository;

    /**
     * Lädt den Posts zu der übergebenen ID aus dem Post-Repository
     *
     * @param query enthält die Post-ID des Posts, der geladen werden soll, sowie Parameter zur Sortierung der
     *              Kommentare des gefundenen Posts
     * @return ApplicationResult<Post> den gefundenen Post, oder alternativ Fehlermeldung
     */
    @Override
    public ApplicationResult<Post> getPostById(GetPostByIdQuery query) {
        RepositoryResult<Post> postResult = postRepository.getPostById(UUID.fromString(query.id()), query.includeComments());

        if(postResult.status().equals(RepositoryResult.Status.ENTITY_NOT_FOUND))
            return ApplicationResult.notFound(query.id() + "not found");

        if(postResult.error())
            return ApplicationResult.exception("Cannot get Post");

        Comparator<Comment> sortComparator = new SortByDate<>();
        if (SortingParams.valueOf(query.sortingParams()) == SortingParams.VOTES)
            sortComparator = new SortByUpvotes<>();

        boolean descending = OrderParams.valueOf(query.orderParams()) == OrderParams.DESC;
        postResult.get().sortComments(descending, sortComparator);

        return ApplicationResult.ok(postResult.get());
    }
}
