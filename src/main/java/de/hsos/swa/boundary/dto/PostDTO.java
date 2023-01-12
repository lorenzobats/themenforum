package de.hsos.swa.boundary.dto;

import de.hsos.swa.entity.Post;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

//TODO immutable machen
public class PostDTO {
    @NotBlank(message = "Post ID is missing")
    public String id;

    @NotBlank(message = "Title is missing")
    public String title;

//    @NotBlank(message = "Posts Content is missing")
//    public String postContent;

    public PostDTO() {}

    public PostDTO(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public static class Converter {
        public static PostDTO toDto(Post entity) {
            return new PostDTO(
                entity.getId().toString(),
                entity.getTitle()
            );
        }
        public static Post toEntity(PostDTO dto) {
            return new Post(
                    UUID.fromString(dto.id),
                    dto.title
            );
        }
    }
}
