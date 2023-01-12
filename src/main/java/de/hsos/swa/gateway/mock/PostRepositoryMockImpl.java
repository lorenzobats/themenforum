package de.hsos.swa.gateway.mock;

import de.hsos.swa.entity.Post;
import de.hsos.swa.entity.repository.PostRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class PostRepositoryMockImpl implements PostRepository {

    private final Map<String, Post> posts = new ConcurrentHashMap<>();

    public PostRepositoryMockImpl() {
        this.posts.put("1", new Post("Post 1"));
        this.posts.put("2", new Post("Post 2"));
        this.posts.put("3", new Post("Post 3"));
    }

    @Override
    public Collection<Post> getAllPosts() {
        return this.posts.values();
    }
}
