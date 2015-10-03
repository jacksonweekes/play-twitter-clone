package model.Exceptions;

/**
 * Used when a email or username clashes with one already registered in datastore
 *
 * @author Jackson Cleary
 */
public class RegistrationException extends Exception {

    private RegistrationErrorCode errorCode;

    /**
     * Constructor
     *
     * @param e An {@link RegistrationErrorCode} representing the registration issue
     */
    public RegistrationException(RegistrationErrorCode e) {
        errorCode = e;
    }

    /**
     * Gets the {@link RegistrationErrorCode} of the exception
     *
     * @return the {@link RegistrationErrorCode} of the exception
     */
    public RegistrationErrorCode getErrorCode() {
        return errorCode;
    }
}
