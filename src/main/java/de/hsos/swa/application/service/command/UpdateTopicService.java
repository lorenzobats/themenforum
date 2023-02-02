package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.UpdateTopicUseCase;
import de.hsos.swa.application.input.dto.in.UpdateTopicCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class UpdateTopicService implements UpdateTopicUseCase {
    @Override
    public ApplicationResult<Topic> updateTopic(UpdateTopicCommand request) {
        // TODO: Implementieren
        return null;
    }
}
