package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.TokenRepository;
import com.uet.ooadloophole.model.Token;
import com.uet.ooadloophole.service.business_service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token findByTokenString(String tokenString) {
        return tokenRepository.findByTokenString(tokenString);
    }

    @Override
    public void deleteActiveToken(Token token) {

    }
}
