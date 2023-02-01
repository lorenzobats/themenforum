package de.hsos.swa.actors.ui.util;

import de.hsos.swa.domain.entity.Comment;

public record CommentUIDto(
        Comment object,
        int depth
) { }