package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.UpdatePostUseCase;
import de.hsos.swa.application.input.dto.in.UpdatePostCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class UpdatePostService implements UpdatePostUseCase {
    @Override
    public Result<Post> updatePost(UpdatePostCommand request) {
        // TODO: Implementieren
        return null;
    }
}
