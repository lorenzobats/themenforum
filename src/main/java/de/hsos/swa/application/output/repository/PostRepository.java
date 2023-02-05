package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 *
 */
public interface PostRepository {

    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    RepositoryResult<Post> savePost(Post post);

    RepositoryResult<Post> updatePost(Post post);

    RepositoryResult<Post> deletePost(UUID postId);

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    RepositoryResult<List<Post>> getFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments);

    RepositoryResult<Post> getPostById(UUID postId, boolean includeComments);

    RepositoryResult<Post> getPostByCommentId(UUID commentId);
}
