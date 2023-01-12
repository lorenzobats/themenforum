package de.hsos.swa.control;

import de.hsos.swa.entity.Post;
import de.hsos.swa.entity.repository.PostRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;

@ApplicationScoped
public class PostManagement {

    @Inject
    PostRepository postRepository;


    public Collection<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }

}
