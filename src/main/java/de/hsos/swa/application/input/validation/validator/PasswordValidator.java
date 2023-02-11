package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Die Klasse PasswordValidator implementiert die Validierung eines annotierten Request-DTO-Feldes.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see ValidPassword    Die zugehörige Annotation
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private String message;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return constraintValidation(context);

        if (value.isEmpty())
            return constraintValidation(context);

        int length = value.length();
        if(length < 6)
            return constraintValidation(context, "Ensure that the password has a minimum length of 6 characters");
        if(length > 32)
            return constraintValidation(context, "Ensure that the password has a maximum length of 32 characters");
        return true;
    }

    private boolean constraintValidation(ConstraintValidatorContext context) {
        return constraintValidation(context, message);
    }

    private boolean constraintValidation(ConstraintValidatorContext context, String defaultMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(defaultMessage)
                .addConstraintViolation();
        return false;
    }
}