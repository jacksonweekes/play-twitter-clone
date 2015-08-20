package model.Exceptions;

/**
 * Created by jackson on 13/08/15.
 */
public enum ErrorCode {
    DUPLICATE_EMAIL(0, "This email address has already been registered."),
    DUPLICATE_USERNAME(1, "This username is already taken."),
    EMAIL_NOT_FOUND(2, "This email address has not been registered."),
    INCORRECT_PASSWORD(3, "Incorrect email/password combination.");

    private final int code;
    private final String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
