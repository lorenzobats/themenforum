package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPostTitle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Die Klasse PostTitleValidator implementiert die Validierung eines annotierten Request-DTO-Feldes.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see ValidPostTitle    Die zugehörige Annotation
 */
public class PostTitleValidator implements ConstraintValidator<ValidPostTitle, String> {
    private String message;
    private boolean nullable;

    @Override
    public void initialize(ValidPostTitle annotation) {
        message = annotation.message();
        nullable = annotation.nullable();
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null && nullable)
            return true;

        if(title == null) {
            return false;
        }

        if(title.length() < 2 || title.length() > 40) {
            return constraintValidation(context, "Post Title must be between 2 and 40 characters");
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
