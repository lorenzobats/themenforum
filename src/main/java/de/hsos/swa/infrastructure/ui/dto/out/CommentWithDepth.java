package de.hsos.swa.infrastructure.ui.dto.out;

import de.hsos.swa.domain.entity.Comment;

public record CommentWithDepth(
        Comment object,
        int depth
) { }