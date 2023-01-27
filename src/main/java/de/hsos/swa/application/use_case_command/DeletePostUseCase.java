package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeletePostInputPort;
import de.hsos.swa.application.input.dto.in.DeletePostInputPortRequest;
import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

/**
 * Die UseCase Klasse DeletePostUseCase implementiert das Interface
 * DeletePostInputPort der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen eines Beitrags durch seinen Ersteller bzw. einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeletePostInputPort             Korrespondierende Input-Port für diesen Use Case
 * @see DeletePostInputPortRequest      Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Verwendeter Output-Port zum Löschen des Beitrags
 * @see GetUserAuthRoleOutputPort       Verwendeter Output-Port zum Laden der Rolle des anfragenden Nutzers
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class DeletePostUseCase implements DeletePostInputPort {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    GetUserAuthRoleOutputPort userAuthRoleOutputPort;


    /**
     * Löscht ein Post auf Basis der übergebenen Informationen.
     * @param request enthält Post-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Post> enthält gelöschten Beitrag bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Post> deletePost(DeletePostInputPortRequest request) {
        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.postId()), false);
        if (!postResult.isSuccessful()) {
            return Result.error("Cannot find post" + request.postId());
        }
        Post post = postResult.getData();


        Result<User> userResult = this.userRepository.getUserByName(request.username());
        if (!userResult.isSuccessful()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = userResult.getData();

        Result<String> roleResult = this.userAuthRoleOutputPort.getUserAuthRole(user.getId());
        if (!roleResult.isSuccessful()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.getData();


        if(!post.getCreator().getId().equals(user.getId()) && !role.equals("admin")){
            return Result.error("Not allowed to delete post");
        }

        Result<Post> deletePostResult = this.postRepository.deletePost(post.getId());
        if (!deletePostResult.isSuccessful()) {
            return Result.error("Cannot delete post ");
        }

        return Result.success(deletePostResult.getData());
    }
}
