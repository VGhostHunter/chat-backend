package com.dhy.chat.web.service.email.impl;

import com.dhy.chat.web.service.email.IEmailService;
import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author vghosthunter
 */
@Service
@ConditionalOnProperty(prefix = "app.provider.email-provider", name = "name", havingValue = "api")
public class ApiEmailService implements IEmailService {

    private final SendGrid sendGrid;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ApiEmailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    @Override
    public void send(String email, String msg) {
        var from = new Email("admin@chat.com");
        var subject = "chat app 登录验证码";
        var to = new Email(email);
        var content = new Content("text/plain", "验证码为:" + msg);
        var mail = new Mail(from, subject, to, content);
        var request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {
                log.info("邮件发送成功");
            } else {
                log.error(response.getBody());
            }
        } catch (IOException e) {
            log.error("请求发生异常 {}", e.getLocalizedMessage());
        }
    }
}
