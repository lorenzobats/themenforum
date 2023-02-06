package de.hsos.swa.application.input.command;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeletePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;
import java.util.Optional;

@InputPort
public interface DeletePostUseCase {
    ApplicationResult<Optional<Post>> deletePost(@Valid DeletePostCommand command, String requestingUser);
}
