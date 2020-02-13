package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    Token findByTokenString(String tokenString);

    void deleteActiveToken(Token token);


}
