package de.hsos.swa.infrastructure.rest.validation;

import de.hsos.swa.infrastructure.rest.dto.in.CommentPostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.in.ReplyToCommentRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.in.VoteCommentRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class CommentValidationService {



    public void validateComment(@Valid CommentPostRestAdapterRequest request) {
    }

    public void validateReply(@Valid ReplyToCommentRestAdapterRequest request) {

    }

    public void validateVote(@Valid VoteCommentRestAdapterRequest request) {

    }

}
