package de.hsos.swa.actors.ui;

import de.hsos.swa.domain.entity.Comment;

public record CommentWithDepth(
        Comment object,
        int depth
) { }