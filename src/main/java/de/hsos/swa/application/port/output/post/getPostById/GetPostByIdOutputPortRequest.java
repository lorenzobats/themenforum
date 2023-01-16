package de.hsos.swa.application.port.output.post.getPostById;

public class GetPostByIdOutputPortRequest {

    private final String id;

    public GetPostByIdOutputPortRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
