package de.hsos.swa.domain.factory;

import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class TopicFactory {
    public static Topic createTopic(@NotBlank String title, @NotBlank String description, @Valid User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Topic>> violations = validator.validate(new Topic(title, description, user));

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return new Topic(title, description, user);
    }
}
