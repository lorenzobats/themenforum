package de.hsos.swa.adapter.input.rest.dto;

import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPortResponse;

import java.util.List;

public class GetCommentByIdRestAdapterResponse {
    public String title;
    public String username;

    public List<String> comments;


    public GetCommentByIdRestAdapterResponse() {
    }

    public GetCommentByIdRestAdapterResponse(String title, String username) {
        this.title = title;
        this.username = username;
    }

    public static class Converter {
        public static GetCommentByIdRestAdapterResponse fromUseCaseResult(GetCommentByIdInputPortResponse result) {
            return new GetCommentByIdRestAdapterResponse(result.getTitle(), result.getUsername());
        }
    }
}
