package de.hsos.swa.domain.validation.constraints;

import de.hsos.swa.domain.validation.validator.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

// QUELLE: https://docs.jboss.org/hibernate/validator/8.0/reference/en-US/html_single/#validator-customconstraints-constraintannotation

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdValidator.class)
public @interface ValidId {
    String message() default "Invalid Id";
    String detail() default "Ensure that the id matches the regular expression [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
