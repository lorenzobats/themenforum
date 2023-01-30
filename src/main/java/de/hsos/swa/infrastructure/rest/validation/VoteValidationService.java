package de.hsos.swa.infrastructure.rest.validation;

import de.hsos.swa.infrastructure.rest.dto.in.VoteEntityRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class VoteValidationService {
    public void validateVote(@Valid VoteEntityRestAdapterRequest request) {}
}
