package de.hsos.swa.application.port.input.getCommentById;

public class GetCommentByIdInputPortResponse {
    private final String title;
    private final String username;

    //Datum
    //Up/Downvotes

    public GetCommentByIdInputPortResponse(String title, String username) {
        this.title = title;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }
}
