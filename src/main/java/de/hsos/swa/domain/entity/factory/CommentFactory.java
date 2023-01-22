package de.hsos.swa.domain.entity.factory;

import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.User;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

public class CommentFactory {
    public static Comment createComment(@NotBlank String text, @Valid User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        LocalDateTime createdAt = LocalDateTime.now();
        Set<ConstraintViolation<Comment>> violations = validator.validate(new Comment(createdAt, user, text));

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return new Comment(createdAt, user, text);
    }
}
