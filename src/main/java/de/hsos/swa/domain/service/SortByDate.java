package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.Post;

import java.util.Comparator;

public class SortByDate implements Comparator<Post> {
    public int compare(Post a, Post b) {
        int result = b.getCreatedAt().compareTo(a.getCreatedAt());
        if(result == 0) {
            result = b.getTotalVotes().compareTo(a.getTotalVotes());
        }
        return result;
    }
}
