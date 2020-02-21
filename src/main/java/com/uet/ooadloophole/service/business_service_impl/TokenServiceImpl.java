package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.TokenRepository;
import com.uet.ooadloophole.model.Token;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public String createToken(User user) {
        Token newToken = new Token(user);
        tokenRepository.save(newToken);
        return newToken.getTokenString();
    }

    @Override
    public Token getByTokenString(String tokenString) {
        return tokenRepository.findByTokenString(tokenString);
    }

    @Override
    public void deleteActiveToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public boolean isValid(String tokenString) {
        Token token = getByTokenString(tokenString);
        if (token == null) {
            return false;
        } else {
            Calendar calendar = Calendar.getInstance();
            return (token.getExpiryDate().getTime() - calendar.getTime().getTime()) > 0;
        }
    }
}
