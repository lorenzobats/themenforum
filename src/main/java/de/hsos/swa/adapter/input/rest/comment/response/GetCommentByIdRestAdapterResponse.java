package de.hsos.swa.adapter.input.rest.comment.response;

import de.hsos.swa.adapter.input.rest.dto.CommentDto;
import de.hsos.swa.domain.entity.Comment;

public class GetCommentByIdRestAdapterResponse {
   public CommentDto comment;


    public GetCommentByIdRestAdapterResponse() {
    }

    public GetCommentByIdRestAdapterResponse(CommentDto comment) {
        this.comment = comment;
    }

    public static class Converter {
        public static GetCommentByIdRestAdapterResponse fromInputPortResult(Comment result) {
            return new GetCommentByIdRestAdapterResponse(CommentDto.Converter.toDto(result));
        }
    }
}
