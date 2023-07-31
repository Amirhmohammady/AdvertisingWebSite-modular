package com.mycompany.advertising.api.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Amir on 8/20/2022.
 */
public class TooManyLoginAttempt extends AuthenticationException {
    public TooManyLoginAttempt(String msg, Throwable t) {
        super(msg, t);
    }

    public TooManyLoginAttempt(String msg) {
        super(msg);
    }
}
