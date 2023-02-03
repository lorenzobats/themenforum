package de.hsos.swa.application.service.query.params;

import java.time.LocalDateTime;
import java.util.UUID;

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
