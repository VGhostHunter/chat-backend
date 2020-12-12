package com.dhy.chat.utils;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

/**
 * @author vghosthunter
 */
@Component
public class TotpUtil {

    private static final long TIME_STEP = 60 * 5L;
    private static final int PASSWORD_LENGTH = 6;
    private KeyGenerator keyGenerator;
    private TimeBasedOneTimePasswordGenerator totp;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /*
     * 初始化代码块，Java 8 开始支持。这种初始化代码块的执行在构造函数之前
     * 准确说应该是 Java 编译器会把代码块拷贝到构造函数的最开始。
     */
    {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_LENGTH);
            keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
            // SHA-1 and SHA-256 需要 64 字节 (512 位) 的 key; SHA512 需要 128 字节 (1024 位) 的 key
            keyGenerator.init(512);
        } catch (NoSuchAlgorithmException e) {
            log.error("没有找到算法 {}", e.getLocalizedMessage());
        }
    }

    /**
     * @param time 用于生成 TOTP 的时间
     * @return 一次性验证码
     * @throws InvalidKeyException 非法 Key 抛出异常
     */
    public String createTotp(final Key key, final Instant time) throws InvalidKeyException {
        var format = "%0" + PASSWORD_LENGTH + "d";
        return String.format(format, totp.generateOneTimePassword(key, time));
    }

    public Optional<String> createTotp(final String strKey) {
        try {
            return Optional.of(createTotp(decodeKeyFromString(strKey), Instant.now()));
        } catch (InvalidKeyException e) {
            return Optional.empty();
        }
    }

    /**
     * 验证 TOTP https://zhuanlan.zhihu.com/p/299135419
     *
     * @param code 要验证的 TOTP
     * @return 是否一致
     * @throws InvalidKeyException 非法 Key 抛出异常
     */
    public boolean validateTotp(final Key key, final String code) throws InvalidKeyException {
        var now = Instant.now();
        return createTotp(key, now).equals(code)
                //向后兼容
                || createTotp(key, now.plusSeconds(TIME_STEP)).equals(code);
    }

    public Key generateKey() {
        return keyGenerator.generateKey();
    }

    public String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String encodeKeyToString() {
        return encodeKeyToString(generateKey());
    }

    public Key decodeKeyFromString(String strKey) {
        return new SecretKeySpec(Base64.getDecoder().decode(strKey), totp.getAlgorithm());
    }

    public long getTimeStepInLong() {
        return TIME_STEP;
    }

    public Duration getTimeStep() {
        return totp.getTimeStep();
    }
}
