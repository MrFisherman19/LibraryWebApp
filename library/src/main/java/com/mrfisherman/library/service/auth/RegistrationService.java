package com.mrfisherman.library.service.auth;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mrfisherman.library.exception.InvalidTokenException;
import com.mrfisherman.library.model.dto.RegisterCredentials;
import com.mrfisherman.library.model.entity.Role;
import com.mrfisherman.library.model.entity.User;
import com.mrfisherman.library.model.entity.types.UserRole;
import com.mrfisherman.library.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;

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

    @Transactional
    public boolean confirmRegistration(String token) {
        switch (verificationTokenService.verifyToken(token)) {
            case VALID: return enableUser(token);
            case EXPIRED: throw new TokenExpiredException("Token expired");
            case INVALID: throw new InvalidTokenException("Token is invalid");
            default: throw new IllegalStateException("Token in undefined stage!");
        }
    }

    private boolean enableUser(String token) {
        return verificationTokenService.enableUserByToken(token);
    }

    private void callOnRegistrationCompleteEvent(User registered) {
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
    }
}
