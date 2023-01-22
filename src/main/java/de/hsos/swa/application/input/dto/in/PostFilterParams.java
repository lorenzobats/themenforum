package de.hsos.swa.application.input.dto.in;

import javax.swing.*;

// TODO: Wo ist diese Klasse angesiedelt (Domain, Application)?
public enum PostFilterParams {
    INCLUDE_COMMENTS(Boolean.class),
    USERNAME(String.class),
    DATE_FROM(String.class),
    DATE_TO(String.class),
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
