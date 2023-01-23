package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.DeletePostVoteInputPort;
import de.hsos.swa.application.input.dto.in.DeletePostVoteInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

public class DeletePostVoteUseCase implements DeletePostVoteInputPort {

    @Override
    public Result<Post> deletePostVote(DeletePostVoteInputPortRequest request) {
        // 1. User via request.username und UserRepository
        // 2. Post via request.postId und PostRepository
        // 3. Domain Service nutzen um Vote des Users an Post zu loeschen
        // 4. Post via PostRepository saven und zurückgeben
        return null;
    }
}
