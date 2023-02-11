package de.hsos.swa.application.input.validation.constraints;

import de.hsos.swa.application.input.validation.validator.TopicDescriptionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Die Annotation ValidTopicDescription definiert die Annotation zur Validierung eines Request-DTO-Feldes.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see TopicDescriptionValidator                           Implementierende Validator-Klasse
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TopicDescriptionValidator.class)
public @interface ValidTopicDescription {
    String message() default "Invalid Topic Description";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
