package de.hsos.swa.gateway.db;

import de.hsos.swa.entity.Post;
import de.hsos.swa.entity.repository.PostRepository;
import de.hsos.swa.gateway.db.dto.Post_JpaEntity;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class PostRepositoryImpl implements PostRepository{

    @Inject
    Logger log;

    @Inject
    EntityManager entityManager;

    @Override
    public Collection<Post> getAllPosts() {
        String queryString = "SELECT p FROM Post p";
        TypedQuery<Post_JpaEntity> query = this.entityManager.createQuery(queryString, Post_JpaEntity.class);
        List<Post_JpaEntity> resultList = query.getResultList();
        return resultList.stream()
                .map(Post_JpaEntity.Converter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Post> getPostById(UUID id) {
        TypedQuery<Post_JpaEntity> query = entityManager.createNamedQuery("PostJpaEntity.getPostById", Post_JpaEntity.class);
        query.setParameter("id", id);
        Post_JpaEntity post_jpaEntity;
        try {
            post_jpaEntity = query.getSingleResult();
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.of(Post_JpaEntity.Converter.toEntity(post_jpaEntity));
    }

    @Override
    public Optional<Post> addPost(Post post) {
        Post_JpaEntity postJpaEntity = Post_JpaEntity.Converter.toJpaEntity(post);
        try {
            entityManager.persist(postJpaEntity);
            Post fromDb = Post_JpaEntity.Converter.toEntity(postJpaEntity);
            return Optional.of(fromDb);
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
            log.warn("Post Entity could not be created", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Post> updatePost(Post post) {
        if (getPostById(post.getId()).isPresent()) {
            try {
                Post_JpaEntity postJpaEntity = Post_JpaEntity.Converter.toJpaEntity(post);
                entityManager.merge(postJpaEntity);
                Post fromDb = Post_JpaEntity.Converter.toEntity(postJpaEntity);
                return Optional.of(fromDb);
            } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
                log.warn("Post Entity could not be updated", e);
            }
        }
        return Optional.empty();
    }


    @Override
    public boolean deletePost(UUID id) {
        Optional<Post> post = getPostById(id);
        if (post.isPresent()) {
            try {
                Post_JpaEntity postJpaEntity = entityManager.find(Post_JpaEntity.class, post.get().getId());
                entityManager.remove(postJpaEntity);
                return true;
            } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException e) {
                log.warn("Post Entity could not be deleted", e);
            }
        }
        return false;
    }

}
