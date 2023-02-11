package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPostContent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Die Klasse PostContentValidator implementiert die Validierung eines annotierten Request-DTO-Feldes.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see ValidPostContent    Die zugehörige Annotation
 */
public class PostContentValidator implements ConstraintValidator<ValidPostContent, String> {
    private String message;
    private boolean nullable;

    @Override
    public void initialize(ValidPostContent annotation) {
        message = annotation.message();
        nullable = annotation.nullable();
    }

    @Override
    public boolean isValid(String content, ConstraintValidatorContext context) {
        if (content == null && nullable)
            return true;

        if(content == null) {
            return false;
        }

        if (content.length() < 2 || content.length() > 250) {
            return constraintValidation(context, "Post content must be between 2 and 250 characters");
        }

        return true;
    }

    private boolean constraintValidation(ConstraintValidatorContext context, String defaultMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(defaultMessage)
                .addConstraintViolation();
        return false;
    }
}
