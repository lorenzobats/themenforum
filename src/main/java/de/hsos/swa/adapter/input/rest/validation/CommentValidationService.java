package de.hsos.swa.adapter.input.rest.validation;

import de.hsos.swa.adapter.input.rest.comment.request.CommentPostRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.comment.request.ReplyToCommentRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class CommentValidationService {



    public void validateComment(@Valid CommentPostRestAdapterRequest request) {
    }

    public void validateReply(@Valid ReplyToCommentRestAdapterRequest request) {

    }

}
