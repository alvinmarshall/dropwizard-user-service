package org.cheise_proj.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class GlobalExceptionAdviser extends WebApplicationException {
    public GlobalExceptionAdviser(String message, Response.Status status) {
        super(message, status);
    }
}
