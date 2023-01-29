package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.SortedEntity;

import java.util.Comparator;

public class SortByUpvotes<T extends SortedEntity> implements Comparator<T> {

    public int compare(T a, T b) {
        int result = Integer.compare(b.getTotalVotes(), a.getTotalVotes());
        if(result == 0) {
            result = b.getCreatedAt().compareTo(a.getCreatedAt());
        }
        return result;
    }
}
