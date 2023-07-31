package com.mycompany.advertising.api.exceptions;

/**
 * Created by Amir on 12/17/2021.
 */
public class CreateTokenException extends RuntimeException {
    public CreateTokenException(String message) {
        super(message);
    }

    public CreateTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateTokenException(Throwable cause) {
        super(cause);
    }
}