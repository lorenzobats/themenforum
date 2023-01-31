package de.hsos.swa.actors.rest.validation;

import de.hsos.swa.actors.rest.dto.in.CreatePostRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;


@RequestScoped
public class PostValidationService {

    public void validatePost(@Valid CreatePostRestAdapterRequest request) {
    }

}
