package de.hsos.swa.infrastructure.rest.validation;

import de.hsos.swa.infrastructure.rest.request.CommentPostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.request.ReplyToCommentRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class CommentValidationService {



    public void validateComment(@Valid CommentPostRestAdapterRequest request) {
    }

    public void validateReply(@Valid ReplyToCommentRestAdapterRequest request) {

    }

}