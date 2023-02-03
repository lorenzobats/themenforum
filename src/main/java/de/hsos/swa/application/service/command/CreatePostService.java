package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CreatePostUseCase;
import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die UseCase Klasse CreatePostService implementiert das Interface
 * CreatePostUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Anlegen eines neuen Beitrags durch den Nutzer.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see CreatePostUseCase            Korrespondierende Input-Port für diesen Use Case
 * @see CreatePostCommand     Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                 Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see TopicRepository                Verwendeter Output-Port zum Laden des Themas für den Beitrag
 * @see PostRepository                 Verwendeter Output-Port zum Speichern des erzeugten Beitrags
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class CreatePostService implements CreatePostUseCase {

    @Inject
    UserRepository userRepository;
    @Inject
    TopicRepository topicRepository;
    @Inject
    PostRepository postRepository;

    /**
     * Erstellt einen neuen Beitrag auf Basis der übergebenen Informationen.
     *
     * @param command         enthält Titel, Inhalt und Themen-ID und Nutzernamen für den zu erstellenden Beitrag
     * @param requestingUser
     * @return ApplicationResult<Post> enthält erstellten Beitrag bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Post> createPost(CreatePostCommand command, String requestingUser) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> getUserByNameResponse = this.userRepository.getUserByName(command.username());
        if(getUserByNameResponse.error()) {
            return ApplicationResult.exception("Cannot find user " + command.username());
        }
        User user = getUserByNameResponse.get();


        RepositoryResult<Topic> getTopicByIdResponse = this.topicRepository.getTopicById(UUID.fromString(command.topicId()));
        if(getTopicByIdResponse.error()) {
            return ApplicationResult.exception("Cannot find topic " + command.topicId());
        }
        Topic topic = getTopicByIdResponse.get();


        Post post = PostFactory.createPost(command.title(), command.content(), topic, user);

        RepositoryResult<Post> savePostResult = this.postRepository.savePost(post);
        if (savePostResult.error()) {
            return ApplicationResult.exception("Cannot save post ");
        }
        return ApplicationResult.ok(savePostResult.get());
    }
}
