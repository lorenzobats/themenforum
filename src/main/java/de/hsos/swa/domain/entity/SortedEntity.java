package de.hsos.swa.domain.entity;

import java.time.LocalDateTime;

public interface SortedEntity {
    Integer getTotalVotes();
    LocalDateTime getCreatedAt();
}
