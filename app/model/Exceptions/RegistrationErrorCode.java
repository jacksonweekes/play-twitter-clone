package model.Exceptions;

/**
 * RegistrationErrorCode- contains a code and associated description for different
 * registration errors.
 *
 * @author Jackson Cleary
 */
public enum RegistrationErrorCode {
    DUPLICATE_EMAIL(0, "This email address has already been registered."),
    DUPLICATE_USERNAME(1, "This username is already taken."),
    EMAIL_NOT_FOUND(2, "This email address has not been registered."),
    INCORRECT_PASSWORD(3, "Incorrect email/password combination.");

    private final int code;
    private final String description;

    RegistrationErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Returns the description associated with the RegistrationErrorCode
     *
     * @return description of error
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the code associated with the RegistrationErrorCode
     *
     * @return the error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns a string representation in the form of 'code: description'
     *
     * @return string representation of the RegistrationErrorCode
     */
    @Override
    public String toString() {
        return code + ": " + description;
    }
}
