package org.cheise_proj.exception;

import javax.lang.model.type.ErrorType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomException extends RuntimeException {
    private final ExceptionType type;
    private final String referenceId;
    private final List<ErrorResponse> errors;

    public CustomException(String message) {
        super(message);
        this.type = ExceptionType.INTERNAL_ERROR;
        this.referenceId = "USERS-SYSTEM";
        this.errors = buildError(message);
    }

    public CustomException(String message, ExceptionType type) {
        super(message);
        this.type = type;
        this.referenceId = "USERS-SYSTEM";
        this.errors = buildError(message);
    }

    public CustomException(String message, ExceptionType type, String referenceId) {
        super(message);
        this.type = type;
        this.referenceId = referenceId;
        this.errors = buildError(message);
    }

    public CustomException(String message, ExceptionType type, String referenceId, List<ErrorResponse> errors) {
        super(message);
        this.type = type;
        this.referenceId = referenceId;
        this.errors = errors;
    }


    public ExceptionType getType() {
        return type;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public List<ErrorResponse> getErrors() {
        return errors;
    }

    private List<ErrorResponse> buildError(String message) {
        List<ErrorResponse> list = new ArrayList<>();
        list.add(ErrorResponse.Builder.builder().message(message).build());
        return list;
    }
}
