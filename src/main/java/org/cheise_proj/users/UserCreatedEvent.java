package org.cheise_proj.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class UserCreatedEvent {

    private final long userId;

    private final String email;

    private final Instant dateCreated;


    @JsonCreator
    public UserCreatedEvent(
            @JsonProperty("userId") long userId,
            @JsonProperty("email") String email,
            @JsonProperty("dateCreated") Instant dateCreated
    ) {
        this.userId = userId;
        this.email = email;
        this.dateCreated = dateCreated;
    }

    private UserCreatedEvent(Builder builder) {
        userId = builder.userId;
        email = builder.email;
        dateCreated = builder.dateCreated;
    }


    public static final class Builder {
        private long userId;
        private String email;
        private Instant dateCreated;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder userId(long val) {
            userId = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder dateCreated(Instant val) {
            dateCreated = val;
            return this;
        }

        public UserCreatedEvent build() {
            return new UserCreatedEvent(this);
        }
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return "UserCreationEvent{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
