package com.mrfisherman.library.service.auth;

import com.mrfisherman.library.model.entity.User;
import com.mrfisherman.library.model.pojo.Email;
import com.mrfisherman.library.service.domain.UserService;
import com.mrfisherman.library.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService userService;
    private final EmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.sendConfirmationMessage(event);
    }

    private void sendConfirmationMessage(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(token, user);

        String address = user.getEmail();
        String subject = "verification of registration";
        String confirmationUrl = "http://localhost:8080/registrationConfirm?token=" + token;
        String text = String.format(
                "You got this e-mail because you registered in the LibraryWebApp domain. " +
                        "Click on the link below to confirm your identity:\n\n%s\n\n" +
                        "Thank you for your registration,\nLibraryWebApp team", confirmationUrl);

        emailService.sendMessage(new Email(address, subject, text));
    }
}
