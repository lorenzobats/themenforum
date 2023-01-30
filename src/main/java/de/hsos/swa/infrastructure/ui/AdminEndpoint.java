package de.hsos.swa.infrastructure.ui;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

//TODO Error Templates erstellen
@Path("/ui/admin")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@RolesAllowed({"admin", "member"})
public class AdminEndpoint {

    @GET
    public void admin(@Context SecurityContext securityContext){
        return;
    }
}
