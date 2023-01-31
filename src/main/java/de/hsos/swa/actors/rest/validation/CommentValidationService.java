package de.hsos.swa.actors.rest.validation;

import de.hsos.swa.actors.rest.dto.in.CommentPostRestAdapterRequest;
import de.hsos.swa.actors.rest.dto.in.ReplyToCommentRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class CommentValidationService {

    public void validateComment(@Valid CommentPostRestAdapterRequest request) {
    }

    public void validateReply(@Valid ReplyToCommentRestAdapterRequest request) {

    }
}
