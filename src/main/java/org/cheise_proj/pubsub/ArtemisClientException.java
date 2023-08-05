package org.cheise_proj.pubsub;

import org.cheise_proj.exception.CustomException;
import org.cheise_proj.exception.ExceptionType;

public class ArtemisClientException extends CustomException {
    public ArtemisClientException(Throwable cause, String referenceId) {
        super(cause, ExceptionType.INTERNAL_ERROR, referenceId);
    }
}
