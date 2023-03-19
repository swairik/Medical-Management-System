package com.mms.demo.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Token;
import com.mms.demo.service.TokenService;
import com.mms.demo.repository.TokenRepository;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    TokenRepository repo;

    @Override
    public Token createToken(Token token) {
        return repo.save(token);
    }

    @Override
    public void deleteToken(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Token> getTokenById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<Token> getTokenByIdentifier(String identifier) {
        return repo.findByIdentifier(identifier);
    }

    @Override
    public Optional<Token> revokeToken(Long id) {
        Optional<Token> temp = repo.findById(id);

        if (temp.isEmpty()) {
            return Optional.empty();
        }

        Token token = temp.get();

        token.setIsRevoked(true);
        return Optional.of(repo.save(token));
    }

    @Override
    public Optional<Token> revokeToken(String identifier) {
        Optional<Token> temp = repo.findByIdentifier(identifier);
        if (temp.isEmpty()) {
            return Optional.empty();
        }

        return revokeToken(temp.get().getId());
    }

    @Override
    public Token updateToken(Long id, Token tokenUpdates) {
        Optional<Token> temp = repo.findById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Token token = temp.get();
        token.setIdentifier(tokenUpdates.getIdentifier());
        token.setIsRevoked(tokenUpdates.getIsRevoked());
        token.setType(tokenUpdates.getType());
        token.setExpirationStamp(tokenUpdates.getExpirationStamp());

        return repo.save(token);
    }

}
