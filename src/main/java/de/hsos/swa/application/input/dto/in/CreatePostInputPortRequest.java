package de.hsos.swa.application.input.dto.in;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public record CreatePostInputPortRequest(
        @NotEmpty(message = "Field: 'title' is missing") String title,
        @NotEmpty(message = "Field: 'content' is missing") String content,
        @Valid UUID topicId,
        @NotEmpty(message = "Field: 'username' is missing") String username
) {}
