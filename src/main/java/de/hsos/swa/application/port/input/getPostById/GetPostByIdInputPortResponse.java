package de.hsos.swa.application.port.input.getPostById;

import de.hsos.swa.domain.entity.Comment;

import java.util.ArrayList;
import java.util.List;

public class GetPostByIdInputPortResponse {
    private final String title;
    private final String username;

    //TODO DTO???
    private List<Comment> comments = new ArrayList<>();

    //Datum
    //Up/Downvotes

    public GetPostByIdInputPortResponse(String title, String username, List<Comment> comments) {
        this.title = title;
        this.username = username;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
