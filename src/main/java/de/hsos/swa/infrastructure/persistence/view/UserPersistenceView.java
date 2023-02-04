package de.hsos.swa.infrastructure.persistence.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.infrastructure.persistence.model.UserPersistenceModel;

import java.util.UUID;

@EntityView(UserPersistenceModel.class)
public record UserPersistenceView(
        @IdMapping UUID id,
        String name,
        boolean active
) {
    public static User toDomainEntity(UserPersistenceView view) {
        return new User(view.id, view.name, view.active);
    }
};

