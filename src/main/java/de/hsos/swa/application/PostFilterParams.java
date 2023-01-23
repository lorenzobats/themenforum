package de.hsos.swa.application;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.UUID;

// TODO: Wo ist diese Klasse angesiedelt (Domain, Application)?
public enum PostFilterParams {
    USERNAME(String.class),
    USERID(UUID.class),
    DATE_FROM(LocalDateTime.class),
    DATE_TO(LocalDateTime.class),
    SORT_BY(String.class),
    SORT_ORDER(SortOrder.class);

    private final Class<?> paramType;

    PostFilterParams(Class<?> paramType) {
        this.paramType = paramType;
    }

    public Class<?> getParamType() {
        return paramType;
    }
}
