package de.hsos.swa.infrastructure.authorization.model;

import de.hsos.swa.infrastructure.persistence.model.UserPersistenceModel;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "owner_of_table", schema = "auth")
public class OwnerOf {
    @Id
    @SequenceGenerator(name = "ownerOfIdSequence", initialValue = 100)
    @GeneratedValue(generator = "ownerOfIdSequence")
    Long id;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    AuthUser owner;

    @Column(unique = true, name = "ressource_id")
    UUID ressourceId;

    public OwnerOf() {
    }

    public OwnerOf(AuthUser owner, UUID ressourceId) {
        this.owner = owner;
        this.ressourceId = ressourceId;
    }
}
