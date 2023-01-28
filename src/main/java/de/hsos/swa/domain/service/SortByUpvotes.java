package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.Post;

import java.util.Comparator;

public class SortByUpvotes implements Comparator<Post> {
    public int compare(Post a, Post b) {
        int result = Integer.compare(b.getTotalVotes(), a.getTotalVotes());
        if(result == 0) {
            result = b.getCreatedAt().compareTo(a.getCreatedAt());
        }
        return result;
    }
}
