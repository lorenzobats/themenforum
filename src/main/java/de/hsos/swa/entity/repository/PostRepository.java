package de.hsos.swa.entity.repository;

import de.hsos.swa.entity.Post;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository {
    Collection<Post> getAllPosts();
    Optional<Post> getPostById(UUID id);
}
