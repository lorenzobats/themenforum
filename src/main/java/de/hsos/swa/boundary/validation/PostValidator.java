package de.hsos.swa.boundary.validation;

import de.hsos.swa.boundary.dto.PostCreationDto;
import de.hsos.swa.boundary.dto.PostDto;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;

@ApplicationScoped
public class PostValidator {

    public void validatePostDTO(@Valid PostDto postDTO) {
    }

    public void validatePostCreationDTO(@Valid PostCreationDto postCreationDTO) {
    }

}
