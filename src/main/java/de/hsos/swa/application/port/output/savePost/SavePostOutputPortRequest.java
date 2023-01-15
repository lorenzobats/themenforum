package de.hsos.swa.application.port.output.savePost;

import de.hsos.swa.domain.entity.Post;

public class SavePostOutputPortRequest {

    private final Post post;

    public SavePostOutputPortRequest(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
