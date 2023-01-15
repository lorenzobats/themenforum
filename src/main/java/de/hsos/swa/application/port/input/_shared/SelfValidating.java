package de.hsos.swa.application.port.input._shared;

import javax.validation.*;
import java.util.Set;

// TODO: Annotation
public abstract class SelfValidating<T> {

    private Validator validator;

    public SelfValidating(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
    }
}
