package de.hsos.swa.boundary.dto;

import de.hsos.swa.entity.Post;

import javax.validation.constraints.NotBlank;

public class PostCreationDto {

    @NotBlank(message="Field: 'title' is missing")
    public String title;

    public PostCreationDto() {}

    public PostCreationDto(String id, String title) {
        this.title = title;
    }
}
