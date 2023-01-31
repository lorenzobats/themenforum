package de.hsos.swa.application.annotations;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApplicationService {}
