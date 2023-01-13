
package de.hsos.swa.gateway.db.dto;

import de.hsos.swa.entity.Post;
import de.hsos.swa.entity.User;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Post")
@Table(name = "post_table")
@NamedQuery(name = "PostJpaEntity.getPostById", query = "SELECT p FROM Post p WHERE p.id = :id")
public class Post_JpaEntity {
    @Id
    @GeneratedValue()
    UUID id;

    @Basic
    String title;

    @ManyToOne
    User_JpaEntity user_jpaEntity;

    public Post_JpaEntity() {
    }

    public Post_JpaEntity(String title, User_JpaEntity user_jpaEntity) {
        this.title = title;
        this.user_jpaEntity = user_jpaEntity;
    }

    public static class Converter {
        public static Post toEntity(Post_JpaEntity post_jpaEntity) {
            User user = User_JpaEntity.Converter.toEntity(post_jpaEntity.user_jpaEntity);
            return new Post(post_jpaEntity.title, user);
        }

        public static Post_JpaEntity toJpaEntity(Post post) {
            User_JpaEntity user_jpaEntity = User_JpaEntity.Converter.toJpaEntity(post.getUser());
            return new Post_JpaEntity(post.getTitle(), user_jpaEntity);
        }
    }

}
