package com.dhy.chat.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author vghosthunter
 */
@Component
public class CryptoUtil {

    public String randomAlphanumeric(int targetStringLength) {
        // 数字 '0'
        int leftLimit = 48;
        // 字母 'z'
        int rightLimit = 122;
        var random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                // 过滤掉 Unicode 65 和 90 之间的字符
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

