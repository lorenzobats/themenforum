package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.NotEmpty;

public record CreateTopicInputPortRequest(
        @NotEmpty(message = "Field: 'title' is missing") String title,
        @NotEmpty(message = "Field: 'description' is missing") String description,
        @NotEmpty(message = "Field: 'username' is missing") String username
) {}