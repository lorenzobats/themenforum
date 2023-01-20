package de.hsos.swa.adapter.input.rest.validation;

import de.hsos.swa.adapter.input.rest.user.request.RegisterUserRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class UserValidationService {

    public void validateUser(@Valid RegisterUserRestAdapterRequest request) {
    }
}
