package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.DeleteUserInputPort;
import de.hsos.swa.application.input.dto.in.DeleteUserInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

public class DeleteUserUseCase implements DeleteUserInputPort {
    @Override
    public Result<User> deleteUser(DeleteUserInputPortRequest request) {
        return null;
    }
}
