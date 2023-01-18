package de.hsos.swa.adapter.input.rest.dto;

import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortResponse;
import de.hsos.swa.domain.entity.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        public static GetPostByIdRestAdapterResponse fromUseCaseResult(GetPostByIdInputPortResponse result) {
            List<CommentDto> comments = result.getComments().stream().map(CommentDto.Converter::toDto).collect(Collectors.toList());

            return new GetPostByIdRestAdapterResponse(result.getTitle(), result.getUsername(), comments);
        }
    }
}
