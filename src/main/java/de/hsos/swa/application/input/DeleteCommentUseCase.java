package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.DeleteCommentCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
import java.util.Optional;

@InputPort
public interface DeleteCommentUseCase {
    ApplicationResult<Optional<Comment>> deleteComment(@Valid DeleteCommentCommand command, String requestingUser);
}
