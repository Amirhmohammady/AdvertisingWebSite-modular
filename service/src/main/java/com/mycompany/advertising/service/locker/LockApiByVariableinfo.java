package com.mycompany.advertising.service.locker;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Created by Amir on 8/5/2022.
 */
public class LockApiByVariableinfo {
    private Method method;
    private String value;
    private LocalDateTime time;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
