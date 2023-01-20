package de.hsos.swa.adapter.input.rest.validation;

import de.hsos.swa.adapter.input.rest.post.request.CreatePostRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;


@RequestScoped
public class PostValidationService {

    public void validatePost(@Valid CreatePostRestAdapterRequest request) {
    }

}
