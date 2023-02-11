package de.hsos.swa.application.input.dto.in;
import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;

/**
 * Der Record GetCommentByIdQuery definiert das validierte Request-DTO für den Input Port GetCommentByIdUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.GetCommentByIdUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record GetCommentByIdQuery(
        @ValidId String commentId
) {}