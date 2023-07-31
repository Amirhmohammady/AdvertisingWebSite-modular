package com.mycompany.advertising.api.exceptions;

/**
 * Created by Amir on 11/9/2021.
 */
public class PhoneNumberFormatException extends RuntimeException {
    public PhoneNumberFormatException(String message) {
        super(message);
    }

    public PhoneNumberFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNumberFormatException(Throwable cause) {
        super(cause);
    }
}
