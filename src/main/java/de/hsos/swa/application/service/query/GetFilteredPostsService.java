package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetFilteredPostsUseCase;
import de.hsos.swa.application.input.dto.in.GetFilteredPostsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.service.SortByDate;
import de.hsos.swa.domain.service.SortByUpvotes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Die Application Service Klasse GetFilteredPostsService implementiert das Interface
 * GetFilteredPostsUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden aller Posts aus dem Post-Repository, die bei Bedarf nach
 * verschiedenen Kriterien gefiltert werden können.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetFilteredPostsUseCase             Korrespondierender Input-Port für diesen Service
 * @see GetFilteredPostsQuery                Korrespondierendes Request-DTO für diesen Service
 * @see PostRepository                      Output-Port zum Laden der Posts
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetFilteredPostsService implements GetFilteredPostsUseCase {

    @Inject
    PostRepository postRepository;

    /**
     * Lädt alle gefilterten Posts aus dem Post-Repository und sortiert diese optional.
     *
     * @param query     enthält verschiedene Filter-Parameter und Sortier-Parameter
     * @return ApplicationResult<List<Post>> enthält die gefilterte/sortierte Liste aller Posts des Themenforums
     */
    @Override
    public ApplicationResult<List<Post>> getFilteredPosts(GetFilteredPostsQuery query) {
        RepositoryResult<List<Post>> postsResult = postRepository.getFilteredPosts(query.filterParams(), query.includeComments());

        if (postsResult.error())
            return ApplicationResult.exception();


        List<Post> sortedPosts = new ArrayList<>(postsResult.get());
        switch (SortingParams.valueOf(query.sortingParams())) {
            case VOTES ->
                    sortPosts(sortedPosts, OrderParams.valueOf(query.orderParams()) == OrderParams.ASC, new SortByUpvotes<>());
            case DATE ->
                    sortPosts(sortedPosts, OrderParams.valueOf(query.orderParams()) == OrderParams.ASC, new SortByDate<>());
            default -> {
                return ApplicationResult.notValid("Invalid sorting param: " + query.sortingParams());
            }
        }
        return ApplicationResult.ok(sortedPosts);
    }

    private void sortPosts(List<Post> posts, boolean reversed, Comparator<Post> comparator) {
        posts.sort(comparator);

        if (reversed) {
            Collections.reverse(posts);
        }
    }
}
