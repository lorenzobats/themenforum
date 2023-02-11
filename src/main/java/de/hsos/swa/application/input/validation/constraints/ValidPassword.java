package de.hsos.swa.application.input.validation.constraints;

import de.hsos.swa.application.input.validation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Die Annotation ValidPassword definiert die Annotation zur Validierung eines Request-DTO-Feldes.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see PasswordValidator                           Implementierende Validator-Klasse
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Password must at least be 6 charachters Long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
