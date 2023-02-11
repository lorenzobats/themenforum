package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.query.GetCommentsByUserUseCase;
import de.hsos.swa.application.input.dto.in.GetCommentsByUserQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Die Application Service Klasse GetCommentsByUserService implementiert das Interface
 * GetCommentsByUserUseCase der Boundary des Application-Hexagons.
 * Es realisiert die Applikationslogik für das Laden aller Kommentare aus dem Kommentar-Repository, die zu dem
 * übergebenen Nutzer gehören.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetCommentsByUserUseCase            Korrespondierender Input-Port für diesen Service
 * @see GetCommentsByUserQuery              Korrespondierendes Request-DTO für diesen Service
 * @see CommentRepository                   Output-Port zum Laden des Kommentars
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetCommentsByUserService implements GetCommentsByUserUseCase {

    @Inject
    CommentRepository commentRepository;

    /**
     * Lädt alle Kommentare eines Nutzers aus dem Kommentar-Repository.
     *
     * @param query     enthält den Nutzernamen, dessen Kommentare geladen werden sollen
     * @return ApplicationResult<List<Comment>> enthält die Liste der zum Nutzer gehörenden Kommentare des Themenforums
     */
    @Override
    public ApplicationResult<List<Comment>> getCommentsByUser(GetCommentsByUserQuery query) {
        RepositoryResult<List<Comment>> result = commentRepository.getCommentsByUser(query.username());
        if (result.error())
            return ApplicationResult.exception();

        return ApplicationResult.ok(result.get());
    }
}
