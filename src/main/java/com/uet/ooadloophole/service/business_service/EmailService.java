package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.LoopholeUser;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendActivationEmail(LoopholeUser user) throws BusinessServiceException;

    void sendResetPasswordEmail(LoopholeUser user) throws BusinessServiceException;
}
