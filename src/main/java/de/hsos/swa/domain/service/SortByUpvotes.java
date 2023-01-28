package de.hsos.swa.domain.service;

import de.hsos.swa.domain.entity.Post;

import java.util.Comparator;

public class SortByUpvotes implements Comparator<Post> {
    public int compare(Post a, Post b) {
        return a.getTotalVotes().compareTo(b.getTotalVotes());
    }
}
