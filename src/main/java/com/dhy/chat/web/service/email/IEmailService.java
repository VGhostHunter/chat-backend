package com.dhy.chat.web.service.email;

/**
 * @author vghosthunter
 */
public interface IEmailService {

    /**
     * Send email
     * @param mobile
     * @param msg
     */
    void send(String mobile, String msg);
}
