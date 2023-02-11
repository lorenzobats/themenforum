package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPostFilterParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

/**
 * Die Klasse PostFilterParamsValidator implementiert die Validierung eines annotierten Request-DTO-Feldes.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see ValidPostFilterParams    Die zugehörige Annotation
 */
public class PostFilterParamsValidator implements ConstraintValidator<ValidPostFilterParams, Map<PostFilterParams, Object>> {
    private String message;

    @Override
    public void initialize(ValidPostFilterParams constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Map<PostFilterParams, Object> filterParams, ConstraintValidatorContext context) {
        for (Map.Entry<PostFilterParams, Object> filterParam : filterParams.entrySet()) {
            if (!filterParam.getValue().getClass().equals(filterParam.getKey().getParamType())) {
                return constraintValidation(context, filterParam.getValue() + " doesnt match type:" + filterParam.getKey().getParamType().getName());
            }
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
