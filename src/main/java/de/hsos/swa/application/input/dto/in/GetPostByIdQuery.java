package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidOrderParams;
import de.hsos.swa.application.input.validation.constraints.ValidSortingParams;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.SortingParams;

import javax.validation.constraints.NotNull;

@InputPortRequest
public record GetPostByIdQuery(
        @ValidId String id,
        @NotNull boolean includeComments,
        SortingParams sortingParams,
        OrderParams orderParams
) {}
