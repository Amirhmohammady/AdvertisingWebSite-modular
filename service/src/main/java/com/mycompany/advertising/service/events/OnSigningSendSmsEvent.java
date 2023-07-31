package com.mycompany.advertising.service.events;

import com.mycompany.advertising.repository.entity.UserTo;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Amir on 9/14/2021.
 */
public class OnSigningSendSmsEvent extends ApplicationEvent {
    private UserTo user;

    public OnSigningSendSmsEvent(UserTo user) {
        super(user);

        this.user = user;
    }

    public UserTo getUser() {
        return user;
    }

    public void setUser(UserTo user) {
        this.user = user;
    }
}
