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
    public ApplicationResult<Topic> getTopicById(GetTopicByIdQuery request) {
        RepositoryResult<Topic> topicResult = topicRepository.getTopicById(UUID.fromString(request.topicId()));
        if (topicResult.ok()) {
            return ApplicationResult.ok(topicResult.get());
        }
        return ApplicationResult.exception("Cannot find Topic");
    }
}
