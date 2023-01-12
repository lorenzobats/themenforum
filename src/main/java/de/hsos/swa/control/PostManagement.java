package de.hsos.swa.control;

import de.hsos.swa.entity.Post;
import de.hsos.swa.entity.repository.PostRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PostManagement {

    @Inject
    PostRepository postRepository;


    public Collection<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }

    public Optional<Post> getPostById(UUID id) {

        return postRepository.getPostById(id);
    }

    public Optional<Post> createPost(Post post) {
        return postRepository.addPost(post);
    }

    public Optional<Post> updatePost(Post post) {
        return postRepository.updatePost(post);
    }



    public boolean deletePost(UUID id) {
        return postRepository.deletePost(id);
    }


}
