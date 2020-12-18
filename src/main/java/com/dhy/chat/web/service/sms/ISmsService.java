package com.dhy.chat.web.service.sms;

/**
 * @author vghosthunter
 */
public interface ISmsService {

    /**
     * 发送短信
     * @param mobile
     * @param msg
     */
    void send(String mobile, String msg);
}
