package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;

@InputPortRequest
public record SearchTopicsQuery(
        String searchString
) {}
