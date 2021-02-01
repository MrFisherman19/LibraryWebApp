package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.User;
import com.mrfisherman.library.model.entity.VerificationToken;
import com.mrfisherman.library.persistence.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final VerificationTokenRepository tokenRepository;

    public UserService(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void createVerificationToken(String verificationToken, User user) {
        VerificationToken token = new VerificationToken(verificationToken, user);
        tokenRepository.save(token);
    }

}
