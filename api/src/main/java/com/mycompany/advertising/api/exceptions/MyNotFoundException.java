package com.mycompany.advertising.api.exceptions;

import java.util.function.Supplier;

/**
 * Created by Amir on 6/28/2023.
 */
public class MyNotFoundException extends RuntimeException implements Supplier<RuntimeException> {
    public MyNotFoundException(String message) {
        super(message);
    }

    public MyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public RuntimeException get() {
        return this;
    }
}
