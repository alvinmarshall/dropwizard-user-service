package org.cheise_proj.exception;

public class ErrorResponse {
    private final String message;

    private ErrorResponse(Builder builder) {
        message = builder.message;
    }

    public String getMessage() {
        return message;
    }

    public static final class Builder {
        private String message;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
