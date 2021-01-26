package com.mrfisherman.library.service.email;

import com.mrfisherman.library.model.pojo.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SimpleMailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final String sender;

    public SimpleMailService(JavaMailSender javaMailSender, @Value("${mail.simple.sender}") String sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    @Override
    public void sendMessage(Email email) {
        javaMailSender.send(prepareSimpleMail(email));
    }

    private SimpleMailMessage prepareSimpleMail(Email email) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(email.getReceiver());
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getBody());
        return mailMessage;
    }
}
