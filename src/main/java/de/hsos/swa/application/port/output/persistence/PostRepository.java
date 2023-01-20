package de.hsos.swa.application.port.output;

import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    Result<List<Post>> getAllPosts(boolean includeComments);
    Result<UUID> savePost(Post post);

    Result<Void> deletePost(String postId);
    Result<Post> getPostById(UUID postId);
    Result<Post> getPostById(UUID postId, boolean includeComments);

    Result<UUID> updatePost(Post post);
}
