package com.uet.ooadloophole.service.business_service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendActivationEmail(String email);

    void sendResetPasswordEmail(String email);
}
