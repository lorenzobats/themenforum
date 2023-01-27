package de.hsos.swa.infrastructure.persistence.cte;

import com.blazebit.persistence.CTE;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

// https://persistence.blazebit.com/documentation/1.6/core/manual/en_US/#ctes
@CTE
@Entity
public class CommentCTE {
    private UUID id;
    private CommentPersistenceModel parentComment;

    @Id
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    @ManyToOne
    public CommentPersistenceModel getParentComment() { return parentComment; }
    public void setParentComment(CommentPersistenceModel parentComment) { this.parentComment = parentComment; }
}
