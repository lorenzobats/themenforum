package de.hsos.swa.infrastructure.rest.validation;

import de.hsos.swa.infrastructure.rest.dto.in.CreatePostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.in.VotePostRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;


@RequestScoped
public class PostValidationService {

    public void validatePost(@Valid CreatePostRestAdapterRequest request) {
    }

    public void validateVote(@Valid VotePostRestAdapterRequest request) {
    }

}
