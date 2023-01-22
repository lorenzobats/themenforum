package de.hsos.swa.infrastructure.rest.validation;

import de.hsos.swa.infrastructure.rest.dto.in.CreateTopicRestAdapterRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;


@RequestScoped
public class TopicValidationService {

    public void validateTopic(@Valid CreateTopicRestAdapterRequest request) {
    }

}
