package de.hsos.swa.adapter.input.rest._dto;
import de.hsos.swa.domain.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDto {
    public String title;
    public String username;

    public List<CommentDto> comments = new ArrayList<>();

    public PostDto(String title, String username, List<CommentDto> comments) {
        this.title = title;
        this.username = username;
        this.comments = comments;
    }

    public static class Converter {
        public static PostDto toDto(Post post) {

            List<CommentDto> commentDtos = post.getComments().stream().map(CommentDto.Converter::toDto).toList();

            PostDto postDto = new PostDto(
                    post.getTitle(),
                    post.getUser().getName(),
                    commentDtos
                    );
            return postDto;
        }
    }
}
