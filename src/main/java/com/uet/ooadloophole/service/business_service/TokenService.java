package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Token;
import com.uet.ooadloophole.model.business.User;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    String createToken(User user);

    Token getByTokenString(String tokenString);

    void deleteActiveToken(Token token);

    boolean verifyToken(String tokenString);
}
