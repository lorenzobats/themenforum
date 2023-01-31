package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetAllUsersUseCase;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import java.util.List;
// TODO implementieren Users
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetAllUsersService implements GetAllUsersUseCase {
    @Override
    public Result<List<User>> getAllUsers() {
        return null;
    }
}
