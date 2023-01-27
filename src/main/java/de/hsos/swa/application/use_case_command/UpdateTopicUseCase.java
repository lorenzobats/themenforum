package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.UpdateTopicInputPort;
import de.hsos.swa.application.input.dto.in.UpdateTopicInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class UpdateTopicUseCase implements UpdateTopicInputPort {
    @Override
    public Result<Topic> updateTopic(UpdateTopicInputPortRequest request) {
        return null;
    }
}
