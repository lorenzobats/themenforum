package de.hsos.swa.actors.rest.validation;

import de.hsos.swa.actors.rest.dto.in.VoteEntityRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class VoteValidationService {
    public void validateVote(@Valid VoteEntityRestAdapterRequest request) {}
}
