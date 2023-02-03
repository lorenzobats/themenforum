package de.hsos.swa.application.input.validation.constraints;


import de.hsos.swa.application.input.validation.validator.PostContentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostContentValidator.class)
public @interface ValidPostContent {
    String message() default "Invalid Post Content";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
