package org.cheise_proj.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.cheise_proj.RegexPattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

 class UserDto implements Serializable {

    @NotEmpty
    private final String name;
    @Email(regexp = RegexPattern.EMAIL_PATTERN)
    @NotEmpty
    private final String email;

    @JsonCreator
    public UserDto(@JsonProperty("name") String name, @JsonProperty("email") String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
