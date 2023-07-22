package org.cheise_proj.users;

public class UserCreatedResponse {
    private final Long id;

    private UserCreatedResponse(Builder builder) {
        id = builder.id;
    }

    public static final class Builder {
        private Long id;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public UserCreatedResponse build() {
            return new UserCreatedResponse(this);
        }
    }

    public Long getId() {
        return id;
    }
}
