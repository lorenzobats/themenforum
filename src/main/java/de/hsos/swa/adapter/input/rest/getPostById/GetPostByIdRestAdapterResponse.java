package de.hsos.swa.adapter.input.rest.getPostById;

import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortResponse;

import java.util.List;

public class GetPostByIdRestAdapterResponse {
    public String title;
    public String username;

    public List<String> comments;


    public GetPostByIdRestAdapterResponse() {
    }

    public GetPostByIdRestAdapterResponse(String title, String username) {
        this.title = title;
        this.username = username;
    }

    public static class Converter {
        public static GetPostByIdRestAdapterResponse fromUseCaseResult(GetPostByIdInputPortResponse result) {
            return new GetPostByIdRestAdapterResponse(result.getTitle(), result.getUsername());
        }
    }
}
