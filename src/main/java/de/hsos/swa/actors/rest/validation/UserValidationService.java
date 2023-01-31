package de.hsos.swa.actors.rest.validation;

import de.hsos.swa.actors.rest.dto.in.RegisterUserRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class UserValidationService {

    public void validateUser(@Valid RegisterUserRestAdapterRequest request) {
    }
}
