package de.hsos.swa.application.input.validation.constraints;

import de.hsos.swa.application.input.validation.validator.InputPortRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// QUELLE: https://docs.jboss.org/hibernate/validator/8.0/reference/en-US/html_single/#validator-customconstraints-constraintannotation

@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InputPortRequestValidator.class)
public @interface ValidInputPortRequest {
    String message() default "Request is invalid";
    String detail() default "Ensure that the command has no empty fields";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
