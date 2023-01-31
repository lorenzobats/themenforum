package de.hsos.swa.infrastructure.persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.hsos.swa.application.output.repository.dto.in.VoteOutputPortDto;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.infrastructure.persistence.model.VotePersistenceModel;
import de.hsos.swa.infrastructure.persistence.view.VotePersistenceView;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
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

    // READ
    @Override
    public RepositoryResult<VoteOutputPortDto> getVoteById(UUID voteId) {
        try {
            CriteriaBuilder<VotePersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, VotePersistenceModel.class);
            criteriaBuilder.where("id").eq(voteId);

            CriteriaBuilder<VotePersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(VotePersistenceView.class), criteriaBuilder);
            VotePersistenceView vote = criteriaBuilderView.getSingleResult();

            return RepositoryResult.ok(VotePersistenceView.toOutputPortDto(vote));
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }

    @Override
    public RepositoryResult<List<VoteOutputPortDto>> getAllVotesByUser(String username) {
        try {
            CriteriaBuilder<VotePersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, VotePersistenceModel.class);
            criteriaBuilder.where("userPersistenceModel.name").eq(username);
            CriteriaBuilder<VotePersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(VotePersistenceView.class), criteriaBuilder);
            List<VotePersistenceView> votes = criteriaBuilderView.getResultList();

            return RepositoryResult.ok(votes.stream().map(VotePersistenceView::toOutputPortDto).toList());
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }

    @Override
    public RepositoryResult<List<VoteOutputPortDto>> getAllVotes() {
        try {
            CriteriaBuilder<VotePersistenceModel> criteriaBuilder = criteriaBuilderFactory.create(entityManager, VotePersistenceModel.class);
            CriteriaBuilder<VotePersistenceView> criteriaBuilderView = entityViewManager.applySetting(EntityViewSetting.create(VotePersistenceView.class), criteriaBuilder);
            List<VotePersistenceView> votes = criteriaBuilderView.getResultList();

            return RepositoryResult.ok(votes.stream().map(VotePersistenceView::toOutputPortDto).toList());
        } catch (NoResultException e) {
            return RepositoryResult.notFound();
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            log.error(e);
            return RepositoryResult.error();
        }
    }
}
