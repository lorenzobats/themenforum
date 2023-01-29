package de.hsos.swa.infrastructure.ui.validation;
import de.hsos.swa.infrastructure.ui.dto.in.UIRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;


@RequestScoped
public class UIValidationService {

    public void validateRequest(@Valid UIRequest request) {}

}
