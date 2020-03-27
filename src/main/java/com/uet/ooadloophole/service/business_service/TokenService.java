package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Token;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TokenService {
    String createToken(String userId, String type);

    Token getByTokenString(String tokenString);

    void deleteToken(Token token);

    boolean isValid(String tokenString);

    boolean isTypeValid(String tokenString, String type);

    List<Token> getAll();
}
