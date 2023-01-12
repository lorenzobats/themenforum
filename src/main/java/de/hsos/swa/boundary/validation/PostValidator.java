package de.hsos.swa.boundary.validation;

import de.hsos.swa.boundary.dto.PostCreationDTO;
import de.hsos.swa.boundary.dto.PostDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class PostValidator {

    public void validatePostDTO(@Valid PostDTO postDTO) {
    }

    public void validatePostCreationDTO(@Valid PostCreationDTO postCreationDTO) {
    }

}
