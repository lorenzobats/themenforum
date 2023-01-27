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
    @Id
    public UUID id;
    @ManyToOne
    public CommentPersistenceModel parentComment;
}
