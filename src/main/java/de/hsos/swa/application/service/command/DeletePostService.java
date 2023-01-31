package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeletePostUseCase;
import de.hsos.swa.application.input.dto.in.DeletePostCommand;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeletePostService implementiert das Interface
 * DeletePostUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen eines Beitrags durch seinen Ersteller bzw. einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeletePostUseCase             Korrespondierende Input-Port für diesen Use Case
 * @see DeletePostCommand      Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Verwendeter Output-Port zum Löschen des Beitrags
 * @see AuthorizationGateway       Verwendeter Output-Port zum Laden der Rolle des anfragenden Nutzers
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeletePostService implements DeletePostUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    AuthorizationGateway authorizationGateway;


    /**
     * Löscht ein Post auf Basis der übergebenen Informationen.
     * @param request enthält Post-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Post> enthält gelöschten Beitrag bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Post> deletePost(DeletePostCommand request) {
        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.postId()), false);
        if (!postResult.isSuccessful()) {
            return Result.error("Cannot find post" + request.postId());
        }
        Post post = postResult.getData();


        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = userResult.get();

        AuthorizationResult<String> roleResult = this.authorizationGateway.getUserAuthRole(user.getId());
        if (roleResult.invalid()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.get();


        if(!post.getUser().getId().equals(user.getId()) && !role.equals("admin")){
            return Result.error("Not allowed to delete post");
        }

        Result<Post> deletePostResult = this.postRepository.deletePost(post.getId());
        if (!deletePostResult.isSuccessful()) {
            return Result.error("Cannot delete post ");
        }

        return Result.success(deletePostResult.getData());
    }
}
