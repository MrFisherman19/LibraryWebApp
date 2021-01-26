package com.mrfisherman.library.service.auth;

import com.mrfisherman.library.model.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private LocalDateTime eventTime;

    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
        this.eventTime = LocalDateTime.now();
    }

}
