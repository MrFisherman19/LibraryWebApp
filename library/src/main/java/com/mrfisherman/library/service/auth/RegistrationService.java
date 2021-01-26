package com.mrfisherman.library.service.auth;

import com.mrfisherman.library.model.dto.RegisterCredentials;
import com.mrfisherman.library.model.entity.Role;
import com.mrfisherman.library.model.entity.User;
import com.mrfisherman.library.model.entity.types.UserRole;
import com.mrfisherman.library.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(RegisterCredentials registerCredentials) {

        final User user = new User();
        user.setUsername(registerCredentials.getUsername());
        user.setPassword(passwordEncoder.encode(registerCredentials.getPassword()));
        user.setEmail(registerCredentials.getEmail());
        user.setRoles(Set.of(new Role(UserRole.USER.name())));

        userRepository.save(user);

        callOnRegistrationCompleteEvent(user);
    }

    private void callOnRegistrationCompleteEvent(User registered) {
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
    }
}
