package org.cheise_proj.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.cheise_proj.exception.ErrorResponse;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceResponse {
    private final boolean success;
    private final Object data;
    private final List<ErrorResponse> errors;

    private ResourceResponse(Builder builder) {
        success = builder.success;
        data = builder.data;
        errors = builder.errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public List<ErrorResponse> getErrors() {
        return errors;
    }

    public static final class Builder {
        private boolean success;
        private Object data;
        private List<ErrorResponse> errors;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder success(boolean val) {
            success = val;
            return this;
        }

        public Builder data(Object val) {
            data = val;
            return this;
        }

        public Builder errors(List<ErrorResponse> val) {
            errors = val;
            return this;
        }

        public ResourceResponse build() {
            return new ResourceResponse(this);
        }
    }
}
