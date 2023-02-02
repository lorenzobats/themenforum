package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeletePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
@InputPort
public interface DeletePostUseCase {
    ApplicationResult<Post> deletePost(@Valid DeletePostCommand request);
}
