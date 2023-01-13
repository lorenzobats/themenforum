package de.hsos.swa.control;

import de.hsos.swa.boundary.dto.PostCreationDto;
import de.hsos.swa.boundary.dto.PostDto;
import de.hsos.swa.entity.Post;
import de.hsos.swa.entity.User;
import de.hsos.swa.entity.repository.PostRepository;
import de.hsos.swa.entity.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PostManagement {

    @Inject
    PostRepository postRepository;

    @Inject
    UserManagement userManagement;

    public Collection<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }

    public Optional<Post> getPostById(UUID id) {

        return postRepository.getPostById(id);
    }

    public Optional<Post> createPost(PostCreationDto postCreationDto, String userName) {
        Optional<User> optionalUser = this.userManagement.getUserByName(userName);
        if(optionalUser.isPresent()){
            Post post = new Post(postCreationDto.title, optionalUser.get());
            return postRepository.addPost(post);
        }
        return Optional.empty();
    }

    // TODO: Update
    public Optional<Post> updatePost(PostDto postDto, String userName) {
        Optional<User> optionalUser = this.userManagement.getUserByName(userName);
        if(optionalUser.isPresent()){
            Post post = new Post(postDto.title, optionalUser.get());
            return postRepository.updatePost(post);
        }
        return Optional.empty();
    }

    public boolean deletePost(UUID id) {
        return postRepository.deletePost(id);
    }

}
