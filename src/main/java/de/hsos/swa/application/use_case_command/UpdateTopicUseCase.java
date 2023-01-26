package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.UpdateTopicInputPort;
import de.hsos.swa.application.input.dto.in.UpdateTopicInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class UpdateTopicUseCase implements UpdateTopicInputPort {
    @Override
    public Result<Topic> updateTopic(UpdateTopicInputPortRequest request) {
        return null;
    }
}
