package model.Exceptions;

import model.Exceptions.ErrorCode;

/**
 * Created by jackson on 13/08/15.
 */
public class ApplicationException extends Exception {

    private ErrorCode errorCode;

    public ApplicationException(ErrorCode e) {
        errorCode = e;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
