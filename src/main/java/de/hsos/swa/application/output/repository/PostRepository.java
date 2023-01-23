package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.PostFilterParams;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PostRepository {

    // CRUD
    Result<Post> savePost(Post post);

    Result<List<Post>> getAllFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments);

    Result<Post> getPostById(UUID postId, boolean includeComments);

    Result<Post> updatePost(Post post);

    Result<Boolean> deletePost(UUID postId);

    // AGGREGATE
}
