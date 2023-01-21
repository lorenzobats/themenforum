package de.hsos.swa.domain.factory;

import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class PostFactory {
    public static Post createPost(@NotBlank String title, @NotBlank String description, @Valid Topic topic, @Valid User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Post>> violations = validator.validate(new Post(title, description, topic, user));

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return new Post(title, description, topic, user);
    }
}
