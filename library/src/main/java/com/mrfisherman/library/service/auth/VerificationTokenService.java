package com.mrfisherman.library.service.auth;

import com.mrfisherman.library.exception.InvalidTokenException;
import com.mrfisherman.library.model.entity.VerificationToken;
import com.mrfisherman.library.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VerificationTokenService {

    private VerificationTokenRepository repository;

    @Value("${token.expirationTime.minutes}")
    private int tokenExpiryTimeInMinutes;

    public VerificationTokenService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    public VerificationResult verifyToken(String token) {
        return repository.findByToken(token)
                .map(VerificationToken::getCreated)
                .map(createdTime -> createdTime.plusMinutes(tokenExpiryTimeInMinutes))
                .map(tokeExpirationDate -> LocalDateTime.now().isBefore(tokeExpirationDate)
                        ? VerificationResult.VALID : VerificationResult.EXPIRED)
                .orElse(VerificationResult.INVALID);
    }

    @Transactional
    public boolean enableUserByToken(String token) {
        var verificationTokenOpt = repository.findByToken(token);
        var verificationToken =
                verificationTokenOpt.orElseThrow(() -> new InvalidTokenException("Token is invalid!"));
        verificationToken.getUser().setEnabled(true);
        deleteToken(token);
        return true;
    }

    @Transactional
    public void deleteToken(String token) {
        repository.deleteByToken(token);
    }

}
