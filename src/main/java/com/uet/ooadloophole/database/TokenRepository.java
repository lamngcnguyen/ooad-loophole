package com.uet.ooadloophole.database;

import com.uet.ooadloophole.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
    Token findByTokenString(String tokenString);
}
