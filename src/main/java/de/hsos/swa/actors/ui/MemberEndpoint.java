package de.hsos.swa.actors.ui;


import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;


//TODO Error Templates erstellen

@Path("/ui/member")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@RolesAllowed({"admin", "member"})
public class MemberEndpoint {



    @GET
    public void user(@Context SecurityContext securityContext){
        return;
    }
}
