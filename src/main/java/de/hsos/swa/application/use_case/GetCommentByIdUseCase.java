package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPort;
import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPortRequest;
import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPortResponse;
import de.hsos.swa.application.port.output.CommentRepository;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.UUID;

@RequestScoped
public class GetCommentByIdUseCase implements GetCommentByIdInputPort {

    @Inject
    CommentRepository commentRepository;

    @Override
    public Result<GetCommentByIdInputPortResponse> getCommentById(GetCommentByIdInputPortRequest request) {
        Result<Comment> commentResult = commentRepository.getCommentById(UUID.fromString(request.getId()));
        if (commentResult.isSuccessful()) {
            GetCommentByIdInputPortResponse getCommentByIdInputPortResponse
                    = new GetCommentByIdInputPortResponse(commentResult.getData());
            return Result.success(getCommentByIdInputPortResponse);
        }
        return Result.error("Cannot find Comment");
    }
}
