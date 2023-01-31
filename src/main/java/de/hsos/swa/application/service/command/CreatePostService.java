package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CreatePostUseCase;
import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
     * @param request enthält Titel, Inhalt und Themen-ID und Nutzernamen für den zu erstellenden Beitrag
     * @return Result<Post> enthält erstellten Beitrag bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Post> createPost(CreatePostCommand request) {
        RepositoryResult<User> getUserByNameResponse = this.userRepository.getUserByName(request.username());
        if(getUserByNameResponse.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = getUserByNameResponse.get();


        Result<Topic> getTopicByIdResponse = this.topicRepository.getTopicById(request.topicId());
        if(!getTopicByIdResponse.isSuccessful()) {
            return Result.error("Cannot find topic " + request.topicId());
        }
        Topic topic = getTopicByIdResponse.getData();


        Post post = PostFactory.createPost(request.title(), request.content(), topic, user);

        Result<Post> savePostResult = this.postRepository.savePost(post);
        if (!savePostResult.isSuccessful()) {
            return Result.error("Cannot save post ");
        }
        return Result.success(savePostResult.getData());
    }
}
