package de.hsos.swa.gateway.db;

import de.hsos.swa.entity.Post;
import de.hsos.swa.entity.repository.PostRepository;
import de.hsos.swa.gateway.db.dto.Post_JpaEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class PostRepositoryImpl implements PostRepository{

    @Inject
    EntityManager entityManager;

    @Override
    public Collection<Post> getAllPosts() {
        return null;
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
}
