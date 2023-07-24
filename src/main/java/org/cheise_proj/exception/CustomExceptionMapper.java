package org.cheise_proj.exception;

import org.cheise_proj.resource.ResourceResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CustomExceptionMapper implements ExceptionMapper<CustomException> {
    @Override
    public Response toResponse(CustomException e) {
        ResourceResponse response;
        switch (e.getType()) {
            case INTERNAL_ERROR:
                response = ResourceResponse.Builder.builder().errors(e.getErrors()).build();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
            case UN_PROCESS_ENTITY:
                response = ResourceResponse.Builder.builder().errors(e.getErrors()).build();
                return Response.status(422).entity(response).build();
            default:
                response = ResourceResponse.Builder.builder().errors(e.getErrors()).build();
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
}
