package de.hsos.swa.entity.repository;

import de.hsos.swa.entity.Post;

import java.util.Collection;

public interface PostRepository {
    Collection<Post> getAllPosts();
}
