package de.hsos.swa.boundary.dto;

import de.hsos.swa.entity.Post;

import javax.validation.constraints.NotBlank;

public class PostDTO {
    @NotBlank(message="Field: 'id' is missing")
    public String id;

    @NotBlank(message="Field: 'title' is missing")
    public String title;

    public PostDTO() {}

    public PostDTO(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public static class Converter {

        public static PostDTO toDto(Post post) {
            return new PostDTO(
                post.getId().toString(),
                post.getTitle()
            );
        }
    }
}
