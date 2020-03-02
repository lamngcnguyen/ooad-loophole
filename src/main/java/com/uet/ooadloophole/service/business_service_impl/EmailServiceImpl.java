package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.config.Constants;
import com.uet.ooadloophole.model.business.User;
import com.uet.ooadloophole.service.business_exceptions.BusinessServiceException;
import com.uet.ooadloophole.service.business_service.EmailService;
import com.uet.ooadloophole.service.business_service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendActivationEmail(User user) throws BusinessServiceException {
        try {
            String token = tokenService.createToken(user.get_id());
            String recipientAddress = user.getEmail();

            String subject = "Kích hoạt tài khoản OOAD Loophole";
            String confirmationUrl = Constants.CONFIRMATION_URL + token;
            String message = "Bấm vào link để kích hoạt tài khoản: ";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(recipientAddress);
            mimeMessageHelper.setText(message + confirmationUrl);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new BusinessServiceException("Unable to send confirmation email to: " + user.getEmail() + " " + e.getMessage());
        }
    }

    @Override
    public void sendResetPasswordEmail(User user) throws BusinessServiceException {
        try {
            String token = tokenService.createToken(user.get_id());
            String recipientAddress = user.getEmail();

            String subject = "Đặt lại mật khẩu tài khoản OOAD Loophole";
            String confirmationUrl = Constants.CONFIRMATION_URL + token;
            String message = "Bấm vào link để đặt lại mật khẩu: ";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(recipientAddress);
            mimeMessageHelper.setText(message + confirmationUrl);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new BusinessServiceException("Unable to send confirmation email to: " + user.getEmail() + " " + e.getMessage());
        }
    }
}
