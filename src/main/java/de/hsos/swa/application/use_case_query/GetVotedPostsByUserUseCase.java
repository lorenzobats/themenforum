package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetVotedPostsByUserInputPort;
import de.hsos.swa.application.input.dto.in.GetVotedPostsByUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetVotedPostsByUserUseCase implements GetVotedPostsByUserInputPort {

    @Override
    public Result<List<Post>> getVotedPostsByUser(GetVotedPostsByUserInputPortRequest request) {
        return null;
    }
}
