package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetAllCommentsUseCase;
import de.hsos.swa.application.input.dto.in.GetAllCommentsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Die Application Service Klasse GetAllCommentsService implementiert das Interface
 * GetAllCommentsUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden aller Kommentare aus dem Kommentar-Repository.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetAllCommentsUseCase               Korrespondierender Input-Port für diesen Service
 * @see GetAllCommentsQuery                 Korrespondierendes Request-DTO für diesen Service
 * @see CommentRepository                   Output-Port zum Laden der Kommentare
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllCommentsService implements GetAllCommentsUseCase {
    @Inject
    CommentRepository commentRepository;

    /**
     * Lädt alle Kommentare aus dem Kommentar-Repository.
     *
     * @param query     enthält einen boolean "includeReplies" mit dem bestimmt werden kann,
     *                  ob die jeweiligen Replies des Kommentars angefügt werden sollen
     * @return ApplicationResult<List<Comment>> enthält die Liste aller Kommentare des Themenforums
     */
    @Override
    public ApplicationResult<List<Comment>> getAllComments(GetAllCommentsQuery query) {
        RepositoryResult<List<Comment>> result = commentRepository.getAllComments(query.includeReplies());
        if (result.error())
            return ApplicationResult.exception();

        return ApplicationResult.ok(result.get());
    }
}
