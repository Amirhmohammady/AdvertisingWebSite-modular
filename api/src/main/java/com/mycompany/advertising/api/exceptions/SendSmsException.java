package com.mycompany.advertising.api.exceptions;

/**
 * Created by Amir on 12/29/2021.
 */
public class SendSmsException extends RuntimeException {
    public SendSmsException(String message) {
        super(message);
    }

    public SendSmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendSmsException(Throwable cause) {
        super(cause);
    }
}