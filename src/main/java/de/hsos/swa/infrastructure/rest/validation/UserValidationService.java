package de.hsos.swa.infrastructure.rest.validation;

import de.hsos.swa.infrastructure.rest.dto.in.RegisterUserRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class UserValidationService {

    public void validateUser(@Valid RegisterUserRestAdapterRequest request) {
    }
}
