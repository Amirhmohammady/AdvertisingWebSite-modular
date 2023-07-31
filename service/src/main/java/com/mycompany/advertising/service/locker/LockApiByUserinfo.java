package com.mycompany.advertising.service.locker;


import com.mycompany.advertising.api.dto.UserDto;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Created by Amir on 8/5/2022.
 */
public class LockApiByUserinfo {
    private Method method;
    private UserDto user;
    private LocalDateTime time;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
