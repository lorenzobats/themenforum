package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetCommentByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetCommentByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die Application Service Klasse GetCommentByIdService implementiert das Interface
 * GetCommentByIdUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden eines Kommentars inklusive seiner Replies
 * aus dem Kommentar-Repository.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetCommentByIdUseCase               Korrespondierender Input-Port für diesen Service
 * @see GetCommentByIdQuery                 Korrespondierendes Request-DTO für diesen Service
 * @see CommentRepository                   Output-Port zum Laden des Kommentars
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetCommentByIdService implements GetCommentByIdUseCase {

    @Inject
    CommentRepository commentRepository;

    /**
     * Lädt einen Kommentar inklusive seiner Replies aus dem Kommentar-Repository.
     *
     * @param query enthält die ID des zu ladenden Kommentars
     * @return ApplicationResult<Comment> enthält den Kommentar bzw. alternativ eine Fehlermeldung
     */
    @Override
    public ApplicationResult<Comment> getCommentById(GetCommentByIdQuery query) {
        RepositoryResult<Comment> result = commentRepository.getCommentById(UUID.fromString(query.commentId()), true);
        if (result.error()) {
            if (result.status() == RepositoryResult.Status.ENTITY_NOT_FOUND) {
                return ApplicationResult.notFound("Cannot find comment: " + query.commentId());
            }
            return ApplicationResult.exception();
        }

        return ApplicationResult.ok(result.get());
    }
}
