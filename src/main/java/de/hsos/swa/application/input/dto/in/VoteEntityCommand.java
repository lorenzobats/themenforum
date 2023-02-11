package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidEnumValue;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.domain.vo.VotedEntityType;
import de.hsos.swa.domain.vo.VoteType;

/**
 * Der Record VoteEntityCommand definiert das validierte Request-DTO für den Input Port VoteEntityUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.command.VoteEntityUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record VoteEntityCommand(
        @ValidId String entityId,
        @ValidEnumValue(enumClass = VoteType.class) String voteType,
        @ValidEnumValue(enumClass = VotedEntityType.class) String entityType
) {}
