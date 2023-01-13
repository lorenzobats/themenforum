package de.hsos.swa.boundary.dto;

import de.hsos.swa.entity.Post;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

//TODO immutable machen
public class PostDto {
    @NotBlank(message = "Post ID is missing")
    public String id;

    @NotBlank(message = "Title is missing")
    public String title;

//    @NotBlank(message = "Posts Content is missing")
//    public String postContent;

    public PostDto() {}

    public PostDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public static class Converter {
        public static PostDto toDto(Post entity) {
            return new PostDto(
                entity.getId().toString(),
                entity.getTitle()
            );
        }
    }
}
