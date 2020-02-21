package com.uet.ooadloophole.model;

import com.uet.ooadloophole.model.business.User;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Token {
    private static final int EXPIRATION = 60 * 24;

    @Id
    private String id;
    private String tokenString;
    private Date expiryDate;
    private String userId;

    public Token(String userId) {
        tokenString = UUID.randomUUID().toString();
        expiryDate = calculateExpiryDate();
        this.userId = userId;
    }

    private Date calculateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, Token.EXPIRATION);
        return new Date(calendar.getTime().getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

