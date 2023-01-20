package de.hsos.swa.infrastructure.rest.validation;

import de.hsos.swa.infrastructure.rest.request.CreatePostRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;


@RequestScoped
public class PostValidationService {

    public void validatePost(@Valid CreatePostRestAdapterRequest request) {
    }

}
