package de.hsos.swa.domain.factory;
import de.hsos.swa.domain.entity.User;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

public class UserFactory {
    public static User createUser(String username) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(new User(username));

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return new User(username);
    }
}
