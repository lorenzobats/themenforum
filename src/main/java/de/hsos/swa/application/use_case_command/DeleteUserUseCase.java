package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.DeleteUserInputPort;
import de.hsos.swa.application.input.dto.in.DeleteUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class DeleteUserUseCase implements DeleteUserInputPort {
    @Override
    public Result<User> deleteUser(DeleteUserInputPortRequest request) {
        return null;
    }
}
