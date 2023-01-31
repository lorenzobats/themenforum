package de.hsos.swa.actors.rest.dto.in.validation;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class ValidationService {
    public void validate(@Valid ValidatedRequestBody requestBody) {}
}
