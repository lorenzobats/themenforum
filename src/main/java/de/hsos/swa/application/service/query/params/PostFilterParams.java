package de.hsos.swa.application.service.query.params;

import de.hsos.swa.application.input.dto.in.GetFilteredPostsQuery;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Das Enum PostFilterParams definiert die Möglichkeiten zur Filterung von Posts
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see GetFilteredPostsQuery           Nutzendes Request DTO
 */
public enum PostFilterParams {
    USERNAME(String.class),
    USERID(UUID.class),
    DATE_FROM(LocalDateTime.class),
    DATE_TO(LocalDateTime.class),
    TOPIC(String.class),
    TOPICID(UUID.class);

    private final Class<?> paramType;

    PostFilterParams(Class<?> paramType) {
        this.paramType = paramType;
    }

    public Class<?> getParamType() {
        return paramType;
    }
}
