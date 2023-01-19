package de.hsos.swa.application.port.input.getAllPosts;
import de.hsos.swa.domain.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class GetAllPostsInputPortResponse {
    private List<Post> posts = new ArrayList<>();

    public GetAllPostsInputPortResponse(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
