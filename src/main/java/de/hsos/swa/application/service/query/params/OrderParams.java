package de.hsos.swa.application.service.query.params;

import de.hsos.swa.application.input.dto.in.GetFilteredPostsQuery;

/**
 * Das Enum OrderParams definiert die Möglichkeiten zur Sortierung von Posts und Kommentaren
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.dto.in.GetPostByIdQuery           Nutzendes Request DTO
 * @see GetFilteredPostsQuery       Nutzendes Request DTO
 */
public enum OrderParams {
    DESC,
    ASC
}
