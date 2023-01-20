package de.hsos.swa.adapter.input.rest.comment.response;

import de.hsos.swa.adapter.input.rest._dto.CommentDto;
import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPortResponse;

public class GetCommentByIdRestAdapterResponse {
   public CommentDto comment;


    public GetCommentByIdRestAdapterResponse() {
    }

    public GetCommentByIdRestAdapterResponse(CommentDto comment) {
        this.comment = comment;
    }

    public static class Converter {
        public static GetCommentByIdRestAdapterResponse fromInputPortResult(GetCommentByIdInputPortResponse result) {
            return new GetCommentByIdRestAdapterResponse(CommentDto.Converter.toDto(result.getComment()));
        }
    }
}
