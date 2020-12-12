package com.dhy.chat.web.service.sms.impl;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.sms.AVSMSOption;
import com.dhy.chat.web.config.LeanCloudConfig;
import com.dhy.chat.web.service.sms.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author vghosthunter
 */
@Service
@Import(LeanCloudConfig.class)
@ConditionalOnProperty(prefix = "app.provider.sms-provider", name = "name", havingValue = "leanCloud")
public class LeanCloudSmsService implements ISmsService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void send(String mobile, String msg) {
        var option = new AVSMSOption();
        option.setTtl(10);
        option.setApplicationName("慕课网实战Spring Security");
        option.setOperation("两步验证");
        option.setTemplateName("登录验证");
        option.setSignatureName("慕课网");
        option.setType(AVSMS.TYPE.TEXT_SMS);
        option.setEnvMap(Map.of("smsCode", msg));
        AVSMS.requestSMSCodeInBackground(mobile, option)
                .take(1)
                .subscribe(
                        (res) -> log.info("短信发送成功 {}", res),
                        (err) -> log.error("发送短信时产生服务端异常 {}", err.getLocalizedMessage())
                );
    }
}
