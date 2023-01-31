package de.hsos.swa.application.output.repository;

import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.domain.entity.Post;

import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * TODO: JavaDocs
 */
public interface PostRepository {

    //------------------------------------------------------------------------------------------------------------------
    // COMMANDS
    RepositoryResult<Post> savePost(Post post);

    RepositoryResult<Post> updatePost(Post post);

    RepositoryResult<Post> deletePost(UUID postId);

    //------------------------------------------------------------------------------------------------------------------
    // QUERIES
    RepositoryResult<List<Post>> getAllFilteredPosts(Map<PostFilterParams, Object> filterParams, boolean includeComments);

    RepositoryResult<List<Post>> getAllPosts(boolean includeComments);

    RepositoryResult<Post> getPostById(UUID postId, boolean includeComments);

    RepositoryResult<Post> getPostByCommentId(UUID commentId);
}
