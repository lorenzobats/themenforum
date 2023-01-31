package de.hsos.swa.actors.ui.validation;
import de.hsos.swa.actors.ui.dto.in.UIRequest;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;


@RequestScoped
public class UIValidationService {

    public void validateRequest(@Valid UIRequest request) {}

}
