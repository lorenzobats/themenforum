package de.hsos.swa.application.input.validation.constraints;

import de.hsos.swa.application.input.validation.validator.CommentTextValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CommentTextValidator.class)
public @interface ValidCommentText {
    String message() default "Invalid username, ensure that it doesnt exceed the length of 250 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}