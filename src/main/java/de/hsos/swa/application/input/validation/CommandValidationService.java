package de.hsos.swa.application.input.validation;

import de.hsos.swa.actors.rest.dto.in.CreateTopicRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

@RequestScoped
public class CommandValidationService {
    public void validateTopic(@Valid CreateTopicRestAdapterRequest request) {
    }
}

