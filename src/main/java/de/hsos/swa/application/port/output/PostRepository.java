package de.hsos.swa.application.port.output;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.UUID;

public interface PostRepository {

    Result<UUID> savePost(Post post);

    Result<Void> deletePost(String postId);
    Result<Post> getPostById(UUID postId);
    Result<Post> getPostById(UUID postId, boolean includeComments);

    Result<UUID> updatePost(Post post);
}
