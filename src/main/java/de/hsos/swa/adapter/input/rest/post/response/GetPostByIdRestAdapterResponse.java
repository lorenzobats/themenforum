package de.hsos.swa.adapter.input.rest.post.response;

import de.hsos.swa.adapter.input.rest.dto.CommentDto;
import de.hsos.swa.domain.entity.Post;

import java.util.*;
import java.util.stream.Collectors;

public class GetPostByIdRestAdapterResponse {
    public String title;
    public String username;

    public List<CommentDto> comments = new ArrayList<>();

    public GetPostByIdRestAdapterResponse(String title, String username, List<CommentDto> comments) {
        this.title = title;
        this.username = username;
        this.comments = comments;
    }

    public static class Converter {
        public static GetPostByIdRestAdapterResponse fromUseCaseResult(Post result) {
            List<CommentDto> comments = result.getComments().stream().map(CommentDto.Converter::toDto).collect(Collectors.toList());
            return new GetPostByIdRestAdapterResponse(result.getTitle(), result.getUser().getName(), comments);
        }
    }
}
