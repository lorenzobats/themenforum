package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetPostByCommentIdUseCase;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die Application Service Klasse GetPostByCommentIdService implementiert das Interface
 * GetPostByCommentIdUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden des Posts, der zu dem übergebenen Kommentar gehört
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetPostByCommentIdUseCase               Korrespondierender Input-Port für diesen Service
 * @see GetPostByCommentIdQuery                 Korrespondierendes Request-DTO für diesen Service
 * @see PostRepository                          Output-Port zum Laden des Posts
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetPostByCommentIdService implements GetPostByCommentIdUseCase {

    @Inject
    PostRepository postRepository;

    /**
     * Lädt den Posts zu der übergebenen Kommentar-ID aus dem Post-Repository
     *
     * @param query enthält die Kommentar-ID zu welcher der Post gefunden werden soll
     * @return ApplicationResult<Post> den gefundenen Post, oder alternativ Fehlermeldung
     */
    @Override
    public ApplicationResult<Post> getPostByCommentId(GetPostByCommentIdQuery query) {
        RepositoryResult<Post> result = postRepository.getPostByCommentId(UUID.fromString(query.id()));
        if (result.error()) {
            if (result.status() == RepositoryResult.Status.ENTITY_NOT_FOUND) {
                return ApplicationResult.notFound("Cannot find post for comment: " + query.id());
            }
            return ApplicationResult.exception();
        }
        return ApplicationResult.ok(result.get());
    }
}
