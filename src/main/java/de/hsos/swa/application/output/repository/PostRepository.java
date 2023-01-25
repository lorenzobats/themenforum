package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.use_case_query.PostFilterParams;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PostRepository {

    // CRUD
    Result<Post> savePost(Post post);

    Result<List<Post>> getAllFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments);

    Result<List<Post>> getAllPosts(boolean includeComments);

    Result<Post> getPostById(UUID postId, boolean includeComments);

    Result<Post> getPostByCommentId(UUID commentId);

    Result<Post> updatePost(Post post);

    Result<Post> deletePost(UUID postId);

    // AGGREGATE
}
