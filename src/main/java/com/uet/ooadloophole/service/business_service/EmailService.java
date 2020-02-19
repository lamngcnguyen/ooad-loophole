package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendActivationEmail(User user) throws BusinessServiceException;

    void sendResetPasswordEmail(User user) throws BusinessServiceException;
}
