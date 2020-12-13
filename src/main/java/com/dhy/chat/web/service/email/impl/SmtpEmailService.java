package com.dhy.chat.web.service.email.impl;

import com.dhy.chat.web.service.email.IEmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author vghosthunter
 */
@Service
@ConditionalOnProperty(prefix = "app.provider.email-provider", name = "name", havingValue = "smtp")
public class SmtpEmailService implements IEmailService {

    private final JavaMailSender emailSender;
    private final MailProperties mailProperties;

    public SmtpEmailService(JavaMailSender emailSender,
                            MailProperties mailProperties) {
        this.emailSender = emailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public void send(String email, String msg) {
        var message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(mailProperties.getUsername());
        message.setSubject("chat app 登录验证码");
        message.setText("验证码为:" + msg);
        emailSender.send(message);
    }
}
