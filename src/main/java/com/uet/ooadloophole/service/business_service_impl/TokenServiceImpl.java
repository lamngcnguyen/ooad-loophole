package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.TokenRepository;
import com.uet.ooadloophole.model.Token;
import com.uet.ooadloophole.service.business_service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public String createToken(String userId, String type) {
        Token newToken = new Token(userId, type);
        tokenRepository.save(newToken);
        return newToken.getTokenString();
    }

    @Override
    public Token getByTokenString(String tokenString) {
        return tokenRepository.findByTokenString(tokenString);
    }

    @Override
    public void deleteToken(Token token) {
        try {
            tokenRepository.delete(token);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValid(String tokenString) {
        Token token = getByTokenString(tokenString);
        if (token == null) {
            return false;
        } else {
            Calendar calendar = Calendar.getInstance();
            if ((token.getExpiryDate().getTime() - calendar.getTime().getTime()) > 0) {
                return true;
            } else {
                deleteToken(token);
                return false;
            }
        }
    }

    @Override
    public boolean isTypeValid(String tokenString, String type) {
        Token token = getByTokenString(tokenString);
        if (token == null) {
            return false;
        } else {
            return token.getType().equals(type);
        }
    }

    @Override
    public List<Token> getAll() {
        return tokenRepository.findAll();
    }
}
