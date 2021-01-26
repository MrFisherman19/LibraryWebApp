package com.mrfisherman.library.service.email;

import com.mrfisherman.library.model.pojo.Email;

public interface EmailService {

    void sendMessage(Email email);

}