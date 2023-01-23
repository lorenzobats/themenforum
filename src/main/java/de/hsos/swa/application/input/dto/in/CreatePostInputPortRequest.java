package de.hsos.swa.application.input.dto.in;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public class CreatePostInputPortRequest {

    @NotEmpty(message="Field: 'title' is missing")
    private final String title;

    @NotEmpty(message="Field: 'content' is missing")
    private final String content;

    @Valid
    private final UUID topicId;

    @NotEmpty(message = "Field: 'username' is missing")
    private final String username;

    public CreatePostInputPortRequest(String title, String content, UUID topicId, String username) {
        this.title = title;
        this.content = content;
        this.topicId = topicId;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public UUID getTopicId() {
        return topicId;
    }

    public String getUsername() {
        return username;
    }
}
