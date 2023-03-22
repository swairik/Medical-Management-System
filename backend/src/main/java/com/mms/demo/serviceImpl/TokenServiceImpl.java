package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Token;
import com.mms.demo.repository.TokenRepository;
import com.mms.demo.service.TokenService;

import jakarta.transaction.Transactional;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    TokenRepository repository;

    @Override
    public Token createToken(Token token) {
        return repository.save(token);
    }

    @Override
    public void deleteToken(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Token> getTokenById(Long id) {
        return repository.findById(id);
    }



    @Override
    public Optional<Token> getTokenByIdentifier(String identifier) {
        return repository.findByIdentifier(identifier);
    }

    @Override
    public Optional<Token> revokeToken(Long id) {
        Optional<Token> temp = repository.findById(id);

        if (temp.isEmpty()) {
            return Optional.empty();
        }

        Token token = temp.get();

        token.setIsRevoked(true);
        return Optional.of(repository.save(token));
    }

    @Override
    public Optional<Token> revokeToken(String identifier) {
        Optional<Token> temp = repository.findByIdentifier(identifier);
        if (temp.isEmpty()) {
            return Optional.empty();
        }

        return revokeToken(temp.get().getId());
    }

    @Override
    public Token updateToken(Long id, Token tokenUpdates) {
        Optional<Token> temp = repository.findById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Token token = temp.get();
        token.setIdentifier(tokenUpdates.getIdentifier());
        token.setIsRevoked(tokenUpdates.getIsRevoked());
        token.setType(tokenUpdates.getType());
        token.setExpirationStamp(tokenUpdates.getExpirationStamp());

        return repository.save(token);
    }

    @Scheduled(cron = "${token.clean.interval}")
    @Transactional
    @Async
    public void tokenCleanerScheduler() {
        repository.deleteAllByExpirationStampBefore(LocalDateTime.now());
    }

}
