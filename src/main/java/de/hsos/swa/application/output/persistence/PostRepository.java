package de.hsos.swa.application.output.persistence;

import de.hsos.swa.application.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    // CRUD
    Result<Post> savePost(Post post);
    Result<List<Post>> getAllPosts(boolean includeComments);

    Result<Post> getPostById(UUID postId, boolean includeComments);

    Result<Post> updatePost(Post post);

    Result<Void> deletePost(String postId);

    // AGGREGATE
}
