package org.cheise_proj.pubsub.jms;

import org.cheise_proj.exception.CustomException;
import org.cheise_proj.exception.ExceptionType;

public class JmsContextLookupException extends CustomException {
    public JmsContextLookupException(Throwable cause, String referenceId) {
        super(cause, ExceptionType.INTERNAL_ERROR, referenceId);
    }
}
