package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.output.dto.VotePersistenceDto;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.infrastructure.persistence.model.VotePersistenceModel;
import de.hsos.swa.infrastructure.persistence.view.VotePersistenceView;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(value = Transactional.TxType.MANDATORY)
public class VotePersistenceAdapter implements VoteRepository {

    @Inject
    EntityManager entityManager;
    @Inject
    CriteriaBuilderFactory criteriaBuilderFactory;

    @Inject
    EntityViewManager entityViewManager;

    @Inject
    Logger log;


    @Override
    public Result<VotePersistenceDto> getVoteById(UUID voteId) {
        // TODO: Try Catch
        CriteriaBuilder<VotePersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, VotePersistenceModel.class);
        criteriaBuilder.where("id").eq(voteId);


        VotePersistenceView vote;
        CriteriaBuilder<VotePersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(VotePersistenceView.class), criteriaBuilder);
        vote = criteriaBuilderView.getSingleResult();
        return Result.success(VotePersistenceView.toApplicationDTO(vote));
    }
}
