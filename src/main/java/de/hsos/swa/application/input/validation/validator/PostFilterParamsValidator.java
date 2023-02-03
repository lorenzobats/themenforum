package de.hsos.swa.application.input.validation.validator;

import de.hsos.swa.application.input.validation.constraints.ValidPostFilterParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

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
                return constraintValidation(context, filterParam.getKey().getParamType().getName() + " doesnt match class type");
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
