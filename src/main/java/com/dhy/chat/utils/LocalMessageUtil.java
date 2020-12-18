package com.dhy.chat.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author vghosthunter
 */
@Component
public class LocalMessageUtil {

    private final MessageSource messageSource;

    public LocalMessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String GetMsg(String message) {
        return messageSource.getMessage(message, null, message, LocaleContextHolder.getLocale());
    }
}
