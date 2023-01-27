package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetAllUsersInputPort;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import java.util.List;
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetAllUsersUseCase implements GetAllUsersInputPort {
    @Override
    public Result<List<User>> getAllUsers() {
        return null;
    }
}
