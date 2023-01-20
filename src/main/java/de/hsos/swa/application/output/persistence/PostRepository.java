package de.hsos.swa.application.output.persistence;

import de.hsos.swa.application.input.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    Result<List<Post>> getAllPosts(boolean includeComments);
    Result<Post> savePost(Post post);

    Result<Void> deletePost(String postId);
    Result<Post> getPostById(UUID postId);
    Result<Post> getPostById(UUID postId, boolean includeComments);

    Result<Post> updatePost(Post post);
}
