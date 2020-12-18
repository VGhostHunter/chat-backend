package com.dhy.chat.web.service.sms.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.dhy.chat.web.config.properties.AppProperties;
import com.dhy.chat.web.service.sms.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author vghosthunter
 */
@Service
@ConditionalOnProperty(prefix = "app.provider.sms-provider", name = "name", havingValue = "ali")
public class AliSmsServiceImpl implements ISmsService {

    private final IAcsClient acsClient;
    private final AppProperties appProperties;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public AliSmsServiceImpl(IAcsClient acsClient,
                             AppProperties appProperties) {
        this.acsClient = acsClient;
        this.appProperties = appProperties;
    }

    @Override
    public void send(String mobile, String msg) {
        var request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(appProperties.getProvider().getApiUrl());
        request.setSysAction("SendSms");
        request.setSysVersion("2017-05-25");
        request.putQueryParameter("RegionId", appProperties.getAliSmsProperties().getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "登录验证");
        request.putQueryParameter("TemplateCode", "SMS_1610048");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" +
                msg +
                "\",\"product\":\"chat app\"}");
        try {
            var response = acsClient.getCommonResponse(request);
            log.info("短信发送结果 {}", response.getData());
        } catch (ServerException e) {
            log.error("发送短信时产生服务端异常 {}", e.getLocalizedMessage());
        } catch (ClientException e) {
            log.error("发送短信时产生客户端异常 {}", e.getLocalizedMessage());
        }
    }
}
