package de.hsos.swa.application.port.input.getPostById;

public class GetPostByIdInputPortResponse {
    private final String title;
    private final String username;

    //Datum
    //Up/Downvotes

    public GetPostByIdInputPortResponse(String title, String username) {
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
