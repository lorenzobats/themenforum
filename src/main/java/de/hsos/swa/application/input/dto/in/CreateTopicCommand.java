package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidTopicDescription;
import de.hsos.swa.application.input.validation.constraints.ValidTopicTitle;

/**
 * Der Record CreateTopicCommand definiert das validierte Request-DTO für den Input Port CreateTopicUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.CreateTopicUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record CreateTopicCommand(
        @ValidTopicTitle String title,
        @ValidTopicDescription String description
) {}