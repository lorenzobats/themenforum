package de.hsos.swa.application.input.validation.constraints;

import de.hsos.swa.application.input.validation.validator.PostFilterParamsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Die Annotation ValidPostFilterParams definiert die Annotation zur Validierung eines Request-DTO-Feldes.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see PostFilterParamsValidator                           Implementierende Validator-Klasse
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostFilterParamsValidator.class)
public @interface ValidPostFilterParams {
    String message() default "Invalid PostFilterParams";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
