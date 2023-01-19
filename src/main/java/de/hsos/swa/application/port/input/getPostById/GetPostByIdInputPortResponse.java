package de.hsos.swa.application.port.input.getPostById;

import de.hsos.swa.domain.entity.Comment;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GetPostByIdInputPortResponse {
    //Art Post DTO
    private final String title;
    private final String username;
    private List<Comment> comments = new ArrayList<>();

    //Datum
    //Up/Downvotes

    public GetPostByIdInputPortResponse(String title, String username) {
        this.title = title;
        this.username = username;
    }

    public void setComments(@NotNull List<Comment> comments) {
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
