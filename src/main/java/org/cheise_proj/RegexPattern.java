package org.cheise_proj;

public abstract class RegexPattern {
    private RegexPattern() {
    }

    public static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
}
