package de.hsos.swa.boundary.dto;

import de.hsos.swa.entity.Post;

import javax.validation.constraints.NotBlank;

public class PostCreationDTO {

    @NotBlank(message="Field: 'title' is missing")
    public String title;

    public PostCreationDTO() {}

    public PostCreationDTO(String id, String title) {
        this.title = title;
    }

    public static class Converter {

        public static Post toEntity(PostCreationDTO dto) {
            return new Post(
                dto.title
            );
        }
    }
}
