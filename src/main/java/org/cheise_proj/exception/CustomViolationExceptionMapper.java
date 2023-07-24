package org.cheise_proj.exception;

import io.dropwizard.jersey.validation.JerseyViolationException;
import org.cheise_proj.resource.ResourceResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class CustomViolationExceptionMapper implements ExceptionMapper<JerseyViolationException> {
    @Override
    public Response toResponse(final JerseyViolationException exception) {
        final List<ErrorResponse> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            Path.Node propertyPath = StreamSupport.stream((violation.getPropertyPath().spliterator()), false).reduce((prev, next) -> next).orElse(null);
            errors.add(ErrorResponse.Builder.builder().message(propertyPath + ": " + violation.getMessage()).build());
        }

        ResourceResponse response = ResourceResponse.Builder.builder()
                .errors(errors)
                .build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
