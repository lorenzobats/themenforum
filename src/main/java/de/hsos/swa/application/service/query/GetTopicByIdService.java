package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetTopicByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetTopicByIdService implements GetTopicByIdUseCase {

    @Inject
    TopicRepository topicRepository;

    @Override
    public ApplicationResult<Topic> getTopicById(GetTopicByIdQuery query) {
        RepositoryResult<Topic> result = topicRepository.getTopicById(UUID.fromString(query.topicId()));
        if (result.error()) {
            if (result.status() == RepositoryResult.Status.ENTITY_NOT_FOUND) {
                return ApplicationResult.notFound("Cannot find topic: " + query.topicId());
            }
            return ApplicationResult.exception();
        }
        return ApplicationResult.ok(result.get());
    }
}
