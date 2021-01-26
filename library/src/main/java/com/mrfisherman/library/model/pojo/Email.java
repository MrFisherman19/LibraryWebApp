package com.mrfisherman.library.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Email {

    private String receiver;
    private String subject;
    private String body;

    @Override
    public String toString() {
        return String.format("Email{to=%s, body=%s}", getReceiver(), getBody());
    }
}
