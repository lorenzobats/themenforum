package de.hsos.swa.application.output.dto;

import de.hsos.swa.domain.value_object.Vote;

import java.util.UUID;

public record VoteWithTypeAndParentID(
    Vote vote,
    String parentType,
    UUID parentId
) { }
