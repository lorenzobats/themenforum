package de.hsos.swa.actors.ui.util;
import io.quarkus.qute.Template;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Inject
    Template error403;

    @Override
    @Produces(MediaType.TEXT_HTML)
    public Response toResponse(ForbiddenException exception) {
        return Response.ok(error403.render()).build();
    }

}