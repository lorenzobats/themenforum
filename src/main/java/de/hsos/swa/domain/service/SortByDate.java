package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.SortedEntity;

import java.util.Comparator;

public class SortByDate<T extends SortedEntity> implements Comparator<T> {
    public int compare(T a, T b) {
        int result = b.getCreatedAt().compareTo(a.getCreatedAt());
        if(result == 0) {
            result = b.getTotalVotes().compareTo(a.getTotalVotes());
        }
        return result;
    }
}
