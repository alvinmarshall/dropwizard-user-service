package org.cheise_proj.users;

import java.io.Serializable;

 class UserResponse implements Serializable {
    private final Long id;
    private final String name;
    private final String email;

    private UserResponse(Builder builder) {
        id = builder.id;
        name = builder.name;
        email = builder.email;
    }

    public static UserResponse toResponse(final User user) {
        return Builder.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static final class Builder {
        private Long id;
        private String name;
        private String email;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
