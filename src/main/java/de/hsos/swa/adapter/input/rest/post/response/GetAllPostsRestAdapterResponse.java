package de.hsos.swa.adapter.input.rest.post.response;

import de.hsos.swa.adapter.input.rest._dto.PostDto;
import de.hsos.swa.application.port.input.getAllPosts.GetAllPostsInputPortResponse;

import java.util.ArrayList;
import java.util.List;

public class GetAllPostsRestAdapterResponse {
    public List<PostDto> posts = new ArrayList<>();

    public GetAllPostsRestAdapterResponse(List<PostDto> posts) {
        this.posts = posts;
    }

    public static class Converter {
        public static GetAllPostsRestAdapterResponse fromInputPortResult(GetAllPostsInputPortResponse result) {
            return new GetAllPostsRestAdapterResponse(result.getPosts().stream().map(PostDto.Converter::toDto).toList());
        }
    }
}
