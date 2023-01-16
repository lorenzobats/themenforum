package de.hsos.swa.application.port.output.post.getPostById;

import de.hsos.swa.domain.entity.Post;

public class GetPostByIdOutputPortResponse {
    private final Post post;

    public GetPostByIdOutputPortResponse(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
