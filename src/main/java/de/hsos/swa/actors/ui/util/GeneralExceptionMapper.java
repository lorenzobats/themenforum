package de.hsos.swa.actors.ui.util;
import io.quarkus.qute.Template;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    @Inject
    Template error500;

    @Override
    @Produces(MediaType.TEXT_HTML)
    public Response toResponse(Exception exception) {
        return Response.ok(error500.render()).build();
    }
}