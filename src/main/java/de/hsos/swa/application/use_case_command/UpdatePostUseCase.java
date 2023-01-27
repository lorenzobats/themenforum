package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.UpdatePostInputPort;
import de.hsos.swa.application.input.dto.in.UpdatePostInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class UpdatePostUseCase implements UpdatePostInputPort {
    @Override
    public Result<Post> updatePost(UpdatePostInputPortRequest request) {
        return null;
    }
}
