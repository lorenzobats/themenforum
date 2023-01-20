package de.hsos.swa.adapter.input.rest.post.response;

import de.hsos.swa.adapter.input.rest.dto.PostDto;
import de.hsos.swa.domain.entity.Post;
import java.util.ArrayList;
import java.util.List;

public class GetAllPostsRestAdapterResponse {
    public List<PostDto> posts = new ArrayList<>();

    public GetAllPostsRestAdapterResponse(List<PostDto> posts) {
        this.posts = posts;
    }

    public static class Converter {
        public static GetAllPostsRestAdapterResponse fromInputPortResult(List<Post> result) {
            return new GetAllPostsRestAdapterResponse(result.stream().map(PostDto.Converter::toDto).toList());
        }
    }
}
